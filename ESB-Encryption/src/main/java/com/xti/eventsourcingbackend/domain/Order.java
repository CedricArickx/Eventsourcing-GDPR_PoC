package com.xti.eventsourcingbackend.domain;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    private Long id;
    private CustomerData customer;
    private Product product;
    private Integer quantity;
    private Date date;

    public Order() {
    }

    public Order(Long id, CustomerData customer, Product product, int quantity, Date date) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerData getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerData customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
