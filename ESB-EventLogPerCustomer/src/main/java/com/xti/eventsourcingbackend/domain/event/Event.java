package com.xti.eventsourcingbackend.domain.event;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "events")
public abstract class Event implements Serializable {

    @Id
    @BsonProperty("id")
    private String id;
    private Date date;
    private String operation;

    public Event() {
    }

    public Event(String id, Date date, String operation) {
        this.id = id;
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
