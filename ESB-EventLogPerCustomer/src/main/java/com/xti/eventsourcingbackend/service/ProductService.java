package com.xti.eventsourcingbackend.service;

import com.xti.eventsourcingbackend.domain.Product;
import com.xti.eventsourcingbackend.domain.event.ProductEvent;
import com.xti.eventsourcingbackend.repository.ProductEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {

    @Autowired
    private ProductEventRepository productEventRepository;

    public List<Product> getAll() {
        List<Product> result = new ArrayList<>();

        List<ProductEvent> creates = StreamSupport.stream(productEventRepository.findByOperationOrderByDateAsc("create").spliterator(), false).collect(Collectors.toList());
        List<Long> delete = productEventRepository.findByOperationOrderByDateAsc("delete").stream().map(n -> n.getProduct().getId()).collect(Collectors.toList());

        creates.stream().filter(n -> n.getOperation().equals("create") && !delete.contains(n.getProduct().getId())).forEach(n -> {
            Product p = n.getProduct();

            productEventRepository.findByProduct_IdAndOperationOrderByDateAsc(p.getId(), "update").forEach(m -> {
                p.setName(m.getProduct().getName() != null ? m.getProduct().getName() : p.getName());
                p.setDescription(m.getProduct().getDescription() != null ? m.getProduct().getDescription() : p.getDescription());
                p.setPrice(m.getProduct().getPrice() != null ? m.getProduct().getPrice() : p.getPrice());
            });
            result.add(p);
        });

        return result;
    }

    public Product getById(Long id) {
        ProductEvent delete = productEventRepository.findFirstByProduct_IdAndOperationOrderByDateAsc(id, "delete");
        ProductEvent create = productEventRepository.findFirstByProduct_IdAndOperationOrderByDateAsc(id, "create");

        if (delete != null ||create == null) {
            return null;
        }

        List<ProductEvent> edits = productEventRepository.findByProduct_IdAndOperationOrderByDateAsc(id, "update");

        Product p = create.getProduct();
        edits.forEach(n -> {
            p.setName(n.getProduct().getName() != null ? n.getProduct().getName() : p.getName());
            p.setDescription(n.getProduct().getDescription() != null ? n.getProduct().getDescription() : p.getDescription());
            p.setPrice(n.getProduct().getPrice() != null ? n.getProduct().getPrice() : p.getPrice());
        });

        return p;
    }

    public void create(Product product) {
        Long id = productEventRepository.findFirstByOperationOrderByIdDesc("create") != null ?
                productEventRepository.findFirstByOperationOrderByIdDesc("create").getProduct().getId() + 1 : 1;
        product.setId(id);
        productEventRepository.save(new ProductEvent(null, new Date(), "create", product));
    }

    public void edit(Product product, Long id) {
        product.setId(id);
        productEventRepository.save(new ProductEvent(null, new Date(), "update", product));
    }

    public void delete(Long id) {
        Product p = new Product();
        p.setId(id);
        productEventRepository.save(new ProductEvent(null, new Date(), "delete", p));
    }
}
