package com.xti.eventsourcingbackend.service;

import com.xti.eventsourcingbackend.domain.Customer;
import com.xti.eventsourcingbackend.domain.event.CustomerEvent;
import com.xti.eventsourcingbackend.repository.CustomerEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerService {

    @Autowired
    private CustomerEventRepository customerEventRepository;

    public List<Customer> getAll() {
        List<Customer> result = new ArrayList<>();

        List<CustomerEvent> creates = StreamSupport.stream(customerEventRepository.findByOperationOrderByDateAsc("create").spliterator(), false).collect(Collectors.toList());
        List<Long> delete = customerEventRepository.findByOperationOrderByDateAsc("delete").stream().map(n -> n.getCustomer().getId()).collect(Collectors.toList());

            creates.stream().filter(n -> n.getOperation().equals("create") && !delete.contains(n.getCustomer().getId())).forEach(n -> {
                Customer c = n.getCustomer();

                customerEventRepository.findByCustomer_IdAndOperationOrderByDateAsc(c.getId(), "update").forEach(m -> {
                    c.setFirstName(m.getCustomer().getFirstName() != null ? m.getCustomer().getFirstName() : c.getFirstName());
                    c.setLastName(m.getCustomer().getLastName() != null ? m.getCustomer().getLastName() : c.getLastName());
                    c.setDateOfBirth(m.getCustomer().getDateOfBirth() != null ? m.getCustomer().getDateOfBirth() : c.getDateOfBirth());
                    c.setEmail(m.getCustomer().getEmail() != null ? m.getCustomer().getEmail() : c.getEmail());
                    c.setPhoneNumber(m.getCustomer().getPhoneNumber() != null ? m.getCustomer().getPhoneNumber() : c.getPhoneNumber());
                });
                result.add(c);
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

        Customer c = create.getCustomer();
        edits.forEach(n -> {
            c.setFirstName(n.getCustomer().getFirstName() != null ? n.getCustomer().getFirstName() : c.getFirstName());
            c.setLastName(n.getCustomer().getLastName() != null ? n.getCustomer().getLastName() : c.getLastName());
            c.setDateOfBirth(n.getCustomer().getDateOfBirth() != null ? n.getCustomer().getDateOfBirth() : c.getDateOfBirth());
            c.setEmail(n.getCustomer().getEmail() != null ? n.getCustomer().getEmail() : c.getEmail());
            c.setPhoneNumber(n.getCustomer().getPhoneNumber() != null ? n.getCustomer().getPhoneNumber() : c.getPhoneNumber());
        });

        return c;
    }

    public void create(Customer customer) {
        Long id = customerEventRepository.findFirstByOperationOrderByIdDesc("create") != null ?
                customerEventRepository.findFirstByOperationOrderByIdDesc("create").getCustomer().getId() + 1 : 1;
        customer.setId(id);
        customerEventRepository.save(new CustomerEvent(new Date(), "create", customer));
    }

    public void edit(Customer customer, Long id) {
        customer.setId(id);
        customerEventRepository.save(new CustomerEvent(new Date(), "update", customer));
    }

    public void delete(Long id) {
        Customer c = new Customer();
        c.setId(id);
        customerEventRepository.save(new CustomerEvent(new Date(), "delete", c));
    }

}
