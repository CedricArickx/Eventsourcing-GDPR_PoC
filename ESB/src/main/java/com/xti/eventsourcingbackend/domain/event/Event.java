package com.xti.eventsourcingbackend.domain.event;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "events")
public abstract class Event implements Serializable {

    @Id
    private String id;
    private Date date;
    private String operation;

    public Event() {
    }

    public Event(Date date, String operation) {
        this.date = date;
        this.operation = operation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
