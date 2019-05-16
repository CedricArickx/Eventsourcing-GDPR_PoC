package com.xti.eventsourcingbackend.repository;

import com.xti.eventsourcingbackend.domain.event.OrderEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderEventRepository extends CrudRepository<OrderEvent, String> {

    List<OrderEvent> findByOperationOrderByDateAsc(String type);

    List<OrderEvent> findByOrder_IdAndOperationOrderByDateAsc(Long id, String type);

    OrderEvent findFirstByOrder_IdAndOperationOrderByDateAsc(Long id, String type);

    OrderEvent findFirstByOperationOrderByIdDesc(String operation);

    void deleteAllByOrder_Id(Long id);
}
