package com.xti.eventsourcingbackend.repository;

import com.xti.eventsourcingbackend.domain.event.CustomerEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerEventRepository extends CrudRepository<CustomerEvent, String> {

    List<CustomerEvent> findByOperationOrderByDateAsc(String type);

    List<CustomerEvent> findByCustomer_IdAndOperationOrderByDateAsc(Long id, String type);

    CustomerEvent findFirstByCustomer_IdAndOperationOrderByDateAsc(Long id, String type);

    CustomerEvent findFirstByOperationOrderByIdDesc(String operation);

    void deleteAllByCustomer_Id(Long id);
}
