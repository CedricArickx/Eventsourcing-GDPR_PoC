package com.xti.eventsourcingbackend.service;

import com.xti.eventsourcingbackend.domain.Customer;
import com.xti.eventsourcingbackend.domain.event.CustomerEvent;
import com.xti.eventsourcingbackend.repository.CustomerDataRepository;
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

    @Autowired
    private CustomerDataRepository customerDataRepository;

    public List<Customer> getAll() {
        List<Long> deletes = customerEventRepository.findByOperationOrderByDateAsc("delete").stream().map(n -> n.getCustomer().getId()).collect(Collectors.toList());

        return customerDataRepository.findAllIdNotIn(deletes);
    }

    public Customer getById(Long id) {
        CustomerEvent delete = customerEventRepository.findFirstByCustomer_IdAndOperationOrderByDateAsc(id, "delete");
        CustomerEvent create = customerEventRepository.findFirstByCustomer_IdAndOperationOrderByDateAsc(id, "create");

        if (delete != null || create == null) {
            return null;
        }

        return customerDataRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public void create(Customer c) {
        Long id = customerEventRepository.findFirstByOperationOrderByIdDesc("create") != null ?
                customerEventRepository.findFirstByOperationOrderByIdDesc("create").getCustomer().getId() + 1 : 1;
        c.setId(id);

        customerDataRepository.save(c);

        c.setFirstName(null);
        c.setLastName(null);
        c.setDateOfBirth(null);
        c.setEmail(null);
        c.setPhoneNumber(null);

        customerEventRepository.save(new CustomerEvent(new Date(), "create", c));
    }

    public void edit(Customer c, Long id) {
        c.setId(id);
        customerDataRepository.save(c);

        c.setFirstName(null);
        c.setLastName(null);
        c.setDateOfBirth(null);
        c.setEmail(null);
        c.setPhoneNumber(null);

        customerEventRepository.save(new CustomerEvent(new Date(), "update", c));
    }

    public void delete(Long id) {
        Customer c = new Customer();
        c.setId(id);
        customerEventRepository.save(new CustomerEvent(new Date(), "delete", c));
        customerDataRepository.deleteById(id);
    }

}
