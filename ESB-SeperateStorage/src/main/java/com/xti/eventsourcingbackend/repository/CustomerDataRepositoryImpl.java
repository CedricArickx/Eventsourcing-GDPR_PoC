package com.xti.eventsourcingbackend.repository;

import com.xti.eventsourcingbackend.domain.Customer;
import com.xti.eventsourcingbackend.domain.event.CustomerEvent;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CustomerDataRepositoryImpl implements CustomCustomerDataRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Customer> findAllIdNotIn(List<Long> ids) {
        Query query = entityManager.createNativeQuery("SELECT * FROM customers WHERE id NOT IN (?)", Customer.class);
        query.setParameter(1, ids.toArray());

        return query.getResultList();
    }
}
