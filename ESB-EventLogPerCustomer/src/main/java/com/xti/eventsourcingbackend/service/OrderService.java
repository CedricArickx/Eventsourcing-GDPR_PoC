package com.xti.eventsourcingbackend.service;

import com.xti.eventsourcingbackend.domain.Customer;
import com.xti.eventsourcingbackend.domain.Order;
import com.xti.eventsourcingbackend.domain.Product;
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

        List<OrderEvent> creates = new ArrayList<>();
        List<Long> deletes = new ArrayList<>();

        orderEventRepository.findByOperationOrderByDateAsc("create").forEach(n -> creates.add(n));
        orderEventRepository.findByOperationOrderByDateAsc("delete").forEach(n -> deletes.add(n.getOrder().getId()));

        creates.stream().filter(n -> n.getOperation().equals("create") && !deletes.contains(n.getOrder().getId())).forEach(n -> {
            Order o = n.getOrder();

            orderEventRepository.findByOrder_IdAndOperationOrderByDateAsc(o.getId(), "update").forEach(m -> {
                o.setCustomer(new Customer(o.getCustomer().getId(), null, null, null, null, null));
                o.setDate(m.getOrder().getDate() != null ? m.getOrder().getDate() : o.getDate());
                o.setProduct(new Product(o.getProduct().getId(), null, null, null));
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
            o.setCustomer(new Customer(o.getCustomer().getId(), null, null, null, null, null));
            o.setDate(n.getOrder().getDate() != null ? n.getOrder().getDate() : o.getDate());
            o.setProduct(new Product(o.getProduct().getId(), null, null, null));
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
        orderEventRepository.save(new OrderEvent(null, new Date(), "create", order));
    }

    public void edit(Order order, Long id) {
        Customer c = new Customer();
        c.setId(getById(id).getCustomer().getId());
        order.setId(id);
        order.setCustomer(c);
        orderEventRepository.save(new OrderEvent(null, new Date(), "update", order));
    }

    public void delete(Long id) {
        Order o = new Order();
        Customer c = new Customer();
        c.setId(getById(id).getCustomer().getId());
        o.setId(id);
        o.setCustomer(c);
        orderEventRepository.save(new OrderEvent(null, new Date(), "delete", o));
    }
}
