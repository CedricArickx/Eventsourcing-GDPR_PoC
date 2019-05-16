package com.xti.eventsourcingbackend.controller;

import com.xti.eventsourcingbackend.domain.Product;
import com.xti.eventsourcingbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable("id") Long id) {
        return productService.getById(id);
    }

    @PostMapping
    public void create(@RequestBody Product product) {
        productService.create(product);
    }

    @PutMapping("/{id}")
    public void edit(@RequestBody Product product, @PathVariable("id") Long id) {productService.edit(product, id);}

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        productService.delete(id);
    }
}
