package com.xti.eventsourcingbackend.domain.event;

import com.xti.eventsourcingbackend.domain.CustomerData;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "events")
@TypeAlias("CustomerEvent")
public class CustomerEvent extends Event {

    private CustomerData customer;

    public CustomerEvent() {
    }

    public CustomerEvent(Date date, String type, CustomerData customer) {
        super(date, type);
        this.customer = customer;
    }

    public CustomerData getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerData customer) {
        this.customer = customer;
    }
}
