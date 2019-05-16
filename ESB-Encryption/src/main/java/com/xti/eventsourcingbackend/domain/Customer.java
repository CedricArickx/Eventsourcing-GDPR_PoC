package com.xti.eventsourcingbackend.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Customer implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;

    public Customer() {
    }

    public Customer(CustomerData customerData) {
        this.id = customerData.getId();
        this.firstName = customerData.getFirstName();
        this.lastName = customerData.getLastName();
        try {
            this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(customerData.getDateOfBirth());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.email = customerData.getEmail();
        this.phoneNumber = customerData.getPhoneNumber();
    }

    public Customer(Long id, String firstName, String lastName, Date dateOfBirth, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
