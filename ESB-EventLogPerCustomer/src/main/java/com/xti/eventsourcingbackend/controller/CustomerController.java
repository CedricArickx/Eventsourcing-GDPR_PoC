package com.xti.eventsourcingbackend.controller;

import com.xti.eventsourcingbackend.domain.Customer;
import com.xti.eventsourcingbackend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable("id") Long id) {
        return customerService.getById(id);
    }

    @PostMapping
    public void create(@RequestBody Customer customer) {
        customerService.create(customer);
    }

    @PutMapping("/{id}")
    public void edit(@RequestBody Customer customer, @PathVariable("id") Long id) {customerService.edit(customer, id);}

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        customerService.delete(id);
    }
}
