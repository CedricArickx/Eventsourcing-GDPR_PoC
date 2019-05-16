package com.xti.eventsourcingbackend.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "encryption_keys")
public class EncryptionKey implements Serializable {

    @Id
    private Long id;
    private String value;

    public EncryptionKey() {
    }

    public EncryptionKey(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
