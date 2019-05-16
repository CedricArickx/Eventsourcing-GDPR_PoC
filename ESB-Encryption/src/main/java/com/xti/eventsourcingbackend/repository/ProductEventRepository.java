package com.xti.eventsourcingbackend.repository;

import com.xti.eventsourcingbackend.domain.event.ProductEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductEventRepository extends CrudRepository<ProductEvent, String> {

    List<ProductEvent> findByOperationOrderByDateAsc(String type);

    List<ProductEvent> findByProduct_IdAndOperationOrderByDateAsc(Long id, String type);

    ProductEvent findFirstByProduct_IdAndOperationOrderByDateAsc(Long id, String type);

    ProductEvent findFirstByOperationOrderByIdDesc(String operation);
}
