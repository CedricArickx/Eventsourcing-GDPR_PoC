package com.xti.eventsourcingbackend.service;

import com.xti.eventsourcingbackend.domain.Order;
import com.xti.eventsourcingbackend.domain.event.OrderEvent;
import com.xti.eventsourcingbackend.repository.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderService {

    @Autowired
    private OrderEventRepository orderEventRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    public List<Order> getAll() {
        List<Order> result = new ArrayList<>();

        List<OrderEvent> creates = StreamSupport.stream(orderEventRepository.findByOperationOrderByDateAsc("create").spliterator(), false).collect(Collectors.toList());
        List<Long> delete = orderEventRepository.findByOperationOrderByDateAsc("delete").stream().map(n -> n.getOrder().getId()).collect(Collectors.toList());

        creates.stream().filter(n -> n.getOperation().equals("create") && !delete.contains(n.getOrder().getId())).forEach(n -> {
            Order o = n.getOrder();

            orderEventRepository.findByOrder_IdAndOperationOrderByDateAsc(o.getId(), "update").forEach(m -> {
                o.setCustomer(customerService.getById(m.getOrder().getCustomer().getId() != null ? m.getOrder().getCustomer().getId() : o.getCustomer().getId()));
                o.setDate(m.getOrder().getDate() != null ? m.getOrder().getDate() : o.getDate());
                o.setProduct(productService.getById(m.getOrder().getProduct().getId() != null ? m.getOrder().getProduct().getId() : o.getProduct().getId()));
                o.setQuantity(m.getOrder().getQuantity() != null ? m.getOrder().getQuantity() : o.getQuantity());
            });

            o.setCustomer(customerService.getById(o.getCustomer().getId()) != null ? customerService.getById(o.getCustomer().getId()) : o.getCustomer());
            o.setProduct(productService.getById(o.getProduct().getId()) != null ? productService.getById(o.getProduct().getId()) : o.getProduct());

            result.add(o);
        });

        return result;
    }

    public Order getById(Long id) {
        OrderEvent delete = orderEventRepository.findFirstByOrder_IdAndOperationOrderByDateAsc(id, "delete");
        OrderEvent create = orderEventRepository.findFirstByOrder_IdAndOperationOrderByDateAsc(id, "create");

        if (delete != null || create == null) {
            return null;
        }

        List<OrderEvent> edits = orderEventRepository.findByOrder_IdAndOperationOrderByDateAsc(id, "update");

        Order o = create.getOrder();

        edits.forEach(n -> {
            o.setCustomer(customerService.getById(n.getOrder().getCustomer().getId() != null ? n.getOrder().getCustomer().getId() : o.getCustomer().getId()));
            o.setDate(n.getOrder().getDate() != null ? n.getOrder().getDate() : o.getDate());
            o.setProduct(productService.getById(n.getOrder().getProduct().getId() != null ? n.getOrder().getProduct().getId() : o.getProduct().getId()));
            o.setQuantity(n.getOrder().getQuantity() != null ? n.getOrder().getQuantity() : o.getQuantity());
        });

        o.setCustomer(customerService.getById(o.getCustomer().getId()) != null ? customerService.getById(o.getCustomer().getId()) : o.getCustomer());
        o.setProduct(productService.getById(o.getProduct().getId()) != null ? productService.getById(o.getProduct().getId()) : o.getProduct());

        return o;
    }

    public void create(Order order) {
        Long id = orderEventRepository.findFirstByOperationOrderByIdDesc("create") != null ?
                orderEventRepository.findFirstByOperationOrderByIdDesc("create").getOrder().getId() + 1 : 1;
        order.setId(id);
        orderEventRepository.save(new OrderEvent(new Date(), "create", order));
    }

    public void edit(Order order, Long id) {
        order.setId(id);
        orderEventRepository.save(new OrderEvent(new Date(), "update", order));
    }

    public void delete(Long id) {
        Order o = new Order();
        o.setId(id);
        orderEventRepository.save(new OrderEvent(new Date(), "delete", o));
    }
}
