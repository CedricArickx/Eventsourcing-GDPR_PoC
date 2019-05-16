package com.xti.eventsourcingbackend.controller;

import com.xti.eventsourcingbackend.domain.Order;
import com.xti.eventsourcingbackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable("id") Long id) { return orderService.getById(id);
    }

    @PostMapping
    public void create(@RequestBody Order order) {
        orderService.create(order);
    }

    @PutMapping("/{id}")
    public void edit(@RequestBody Order order, @PathVariable("id") Long id) {orderService.edit(order, id);}

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        orderService.delete(id);
    }
}
