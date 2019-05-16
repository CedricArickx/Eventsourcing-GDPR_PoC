package com.xti.eventsourcingbackend.domain.event;

import com.xti.eventsourcingbackend.domain.Product;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "events")
@TypeAlias("ProductEvent")
public class ProductEvent extends Event {

    private Product product;

    public ProductEvent() {
    }

    public ProductEvent(Date date, String type, Product product) {
        super(date, type);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
