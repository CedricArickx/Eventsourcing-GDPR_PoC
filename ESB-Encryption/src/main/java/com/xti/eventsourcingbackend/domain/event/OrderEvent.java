package com.xti.eventsourcingbackend.domain.event;

import com.xti.eventsourcingbackend.domain.Order;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "events")
@TypeAlias("OrderEvent")
public class OrderEvent extends Event {

    private Order order;

    public OrderEvent() {
    }

    public OrderEvent(Date date, String type, Order order) {
        super(date, type);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
