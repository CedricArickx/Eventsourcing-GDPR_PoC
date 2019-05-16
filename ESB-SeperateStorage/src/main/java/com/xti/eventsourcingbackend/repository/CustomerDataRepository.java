package com.xti.eventsourcingbackend.repository;

import com.xti.eventsourcingbackend.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public interface CustomerDataRepository extends JpaRepository<Customer, Long>, CustomCustomerDataRepository {
}
