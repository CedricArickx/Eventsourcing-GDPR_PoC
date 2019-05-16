package com.xti.eventsourcingbackend.repository;

import com.xti.eventsourcingbackend.domain.Customer;
import com.xti.eventsourcingbackend.domain.event.CustomerEvent;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomCustomerDataRepository {

    List<Customer> findAllIdNotIn(List<Long> ids);
}
