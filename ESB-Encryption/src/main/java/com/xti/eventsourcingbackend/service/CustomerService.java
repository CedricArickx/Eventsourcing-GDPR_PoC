package com.xti.eventsourcingbackend.service;

import com.xti.eventsourcingbackend.domain.Customer;
import com.xti.eventsourcingbackend.domain.CustomerData;
import com.xti.eventsourcingbackend.domain.EncryptionKey;
import com.xti.eventsourcingbackend.domain.event.CustomerEvent;
import com.xti.eventsourcingbackend.repository.CustomerEventRepository;
import com.xti.eventsourcingbackend.repository.EncryptionKeyRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerService {

    @Autowired
    private CustomerEventRepository customerEventRepository;

    @Autowired
    private EncryptionKeyRepository encryptionKeyRepository;

    public List<Customer> getAll() {
        List<Customer> result = new ArrayList<>();

        List<CustomerEvent> creates = StreamSupport.stream(customerEventRepository.findByOperationOrderByDateAsc("create").spliterator(), false).collect(Collectors.toList());
        List<Long> delete = customerEventRepository.findByOperationOrderByDateAsc("delete").stream().map(n -> n.getCustomer().getId()).collect(Collectors.toList());

        creates.stream().filter(n -> n.getOperation().equals("create") && !delete.contains(n.getCustomer().getId())).forEach(n -> {
            CustomerData cd = n.getCustomer();

            customerEventRepository.findByCustomer_IdAndOperationOrderByDateAsc(cd.getId(), "update").forEach(m -> {
                cd.setFirstName(m.getCustomer().getFirstName() != null ? m.getCustomer().getFirstName() : cd.getFirstName());
                cd.setLastName(m.getCustomer().getLastName() != null ? m.getCustomer().getLastName() : cd.getLastName());
                cd.setDateOfBirth(m.getCustomer().getDateOfBirth() != null ? m.getCustomer().getDateOfBirth() : cd.getDateOfBirth());
                cd.setEmail(m.getCustomer().getEmail() != null ? m.getCustomer().getEmail() : cd.getEmail());
                cd.setPhoneNumber(m.getCustomer().getPhoneNumber() != null ? m.getCustomer().getPhoneNumber() : cd.getPhoneNumber());
            });

            EncryptionKey key = encryptionKeyRepository.findById(cd.getId()).orElse(null);

            if (key != null) {
                cd.setFirstName(decryptString(cd.getFirstName(), key.getValue()));
                cd.setLastName(decryptString(cd.getLastName(), key.getValue()));
                cd.setDateOfBirth(decryptString(cd.getDateOfBirth(), key.getValue()));
                cd.setEmail(decryptString(cd.getEmail(), key.getValue()));
                cd.setPhoneNumber(decryptString(cd.getPhoneNumber(), key.getValue()));
            }

            result.add(new Customer(cd));
        });

        return result;
    }

    public Customer getById(Long id) {
        CustomerEvent delete = customerEventRepository.findFirstByCustomer_IdAndOperationOrderByDateAsc(id, "delete");
        CustomerEvent create = customerEventRepository.findFirstByCustomer_IdAndOperationOrderByDateAsc(id, "create");

        if (delete != null || create == null) {
            return null;
        }

        List<CustomerEvent> edits = customerEventRepository.findByCustomer_IdAndOperationOrderByDateAsc(id, "update");

        CustomerData cd = create.getCustomer();
        edits.forEach(n -> {
            cd.setFirstName(n.getCustomer().getFirstName() != null ? n.getCustomer().getFirstName() : cd.getFirstName());
            cd.setLastName(n.getCustomer().getLastName() != null ? n.getCustomer().getLastName() : cd.getLastName());
            cd.setDateOfBirth(n.getCustomer().getDateOfBirth() != null ? n.getCustomer().getDateOfBirth() : cd.getDateOfBirth());
            cd.setEmail(n.getCustomer().getEmail() != null ? n.getCustomer().getEmail() : cd.getEmail());
            cd.setPhoneNumber(n.getCustomer().getPhoneNumber() != null ? n.getCustomer().getPhoneNumber() : cd.getPhoneNumber());
        });

        EncryptionKey key = encryptionKeyRepository.findById(id).orElse(null);

        if (key != null) {
            cd.setFirstName(decryptString(cd.getFirstName(), key.getValue()));
            cd.setLastName(decryptString(cd.getLastName(), key.getValue()));
            cd.setDateOfBirth(decryptString(cd.getDateOfBirth(), key.getValue()));
            cd.setEmail(decryptString(cd.getEmail(), key.getValue()));
            cd.setPhoneNumber(decryptString(cd.getPhoneNumber(), key.getValue()));
        }

        return new Customer(cd);
    }

    public void create(Customer customer) {
        Long id = customerEventRepository.findFirstByOperationOrderByIdDesc("create") != null ?
                customerEventRepository.findFirstByOperationOrderByIdDesc("create").getCustomer().getId() + 1 : 1;
        CustomerData cd = new CustomerData();
        cd.setId(id);

        String generatedString = RandomString.make(24);

        cd.setFirstName(encryptString(customer.getFirstName(), generatedString));
        cd.setLastName(encryptString(customer.getLastName(), generatedString));
        cd.setDateOfBirth(encryptString(new SimpleDateFormat("yyyy-MM-dd").format(customer.getDateOfBirth()), generatedString));
        cd.setEmail(encryptString(customer.getEmail(), generatedString));
        cd.setPhoneNumber(encryptString(customer.getPhoneNumber(), generatedString));

        encryptionKeyRepository.save(new EncryptionKey(id, generatedString));
        customerEventRepository.save(new CustomerEvent(new Date(), "create", cd));
    }

    public void edit(Customer customer, Long id) {
        CustomerData cd = new CustomerData();
        cd.setId(id);

        EncryptionKey generatedString = encryptionKeyRepository.findById(id).orElse(null);

        cd.setFirstName(customer.getFirstName() != null ? encryptString(customer.getFirstName(), generatedString.getValue()) : customer.getFirstName());
        cd.setLastName(customer.getLastName() != null ? encryptString(customer.getLastName(), generatedString.getValue()): customer.getLastName());
        cd.setDateOfBirth(customer.getDateOfBirth() != null ? encryptString(new SimpleDateFormat("yyyy-MM-dd").format(customer.getDateOfBirth()), generatedString.getValue()) : null);
        cd.setEmail(customer.getEmail() != null ? encryptString(customer.getEmail(), generatedString.getValue()) : customer.getEmail());
        cd.setPhoneNumber(customer.getPhoneNumber() != null ? encryptString(customer.getPhoneNumber(), generatedString.getValue()) : customer.getPhoneNumber());

        customerEventRepository.save(new CustomerEvent(new Date(), "update", cd));
    }

    public void delete(Long id) {
        CustomerData cd = new CustomerData();
        cd.setId(id);
        encryptionKeyRepository.deleteById(id);
        customerEventRepository.save(new CustomerEvent(new Date(), "delete", cd));
    }

    public String encryptString(String input, String key) {
        String result = null;
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);

            byte[] encrypted = cipher.doFinal(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : encrypted) {
                sb.append((char) b);
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String decryptString(String input, String key) {
        String result = null;
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);

            byte[] bb = new byte[input.length()];
            for (int i = 0; i < input.length(); i++) {
                bb[i] = (byte) input.charAt(i);
            }

            result = new String(cipher.doFinal(bb));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return result;
    }

}
