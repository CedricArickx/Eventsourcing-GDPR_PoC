package com.xti.eventsourcingbackend.domain.event;

import com.xti.eventsourcingbackend.domain.Customer;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "events")
@TypeAlias("CustomerEvent")
public class CustomerEvent extends Event {

    private Customer customer;

    public CustomerEvent() {
    }

    public CustomerEvent(String id, Date date, String type, Customer customer) {
        super(id, date, type);
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
