package com.xti.eventsourcingbackend.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.xti.eventsourcingbackend.domain.Customer;
import com.xti.eventsourcingbackend.domain.event.CustomerEvent;
import com.xti.eventsourcingbackend.domain.event.Event;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.Filer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Repository
public class CustomerEventRepository {

    private MongoDatabase database;

    public CustomerEventRepository() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClient mongoClient = new MongoClient();
        database = mongoClient.getDatabase("eventsourcing").withCodecRegistry(pojoCodecRegistry);
    }

    public List<CustomerEvent> findByOperationOrderByDateAsc(String type) {
        List<CustomerEvent> result = new ArrayList<>();
        database.listCollectionNames().forEach((Consumer<? super String>) n -> {
            if (n.contains("customer-")) {
                database.getCollection(n, CustomerEvent.class)
                        .find(Filters.eq("operation", type))
                        .sort(descending("date"))
                        .forEach((Consumer<? super CustomerEvent>) m -> {
                            if (m.getOperation().equals(type))
                                result.add(m);
                        });
            }
        });

        return result;
    }

    public List<CustomerEvent> findByCustomer_IdAndOperationOrderByDateAsc(Long id, String type) {
        List<CustomerEvent> result = new ArrayList<>();
        database.getCollection("customer-" + id.toString(), CustomerEvent.class)
                .find(Filters.eq("operation", type))
                .sort(ascending("date"))
                .forEach((Consumer<? super CustomerEvent>) m -> {
                    if (m.getOperation().equals(type))
                        result.add(m);
                });

        return result;
    }

    public CustomerEvent findFirstByCustomer_IdAndOperationOrderByDateAsc(Long id, String type) {
        return database.getCollection("customer-" + id.toString(), CustomerEvent.class)
                .find(Filters.eq("operation", type))
                .sort(ascending("date")).first();
    }

    public CustomerEvent findFirstByOperationOrderByIdDesc(String type) {
        List<Long> ids = new ArrayList<>();

        database.listCollectionNames().forEach((Consumer<? super String>) n -> {
            if (n.contains("customer-")) {
                CustomerEvent ce = database.getCollection(n, CustomerEvent.class)
                        .find(Filters.eq("_class", "CustomerEvent"))
                        .sort(descending("customer._id"))
                        .first();

                if (ce != null) {
                    ids.add(ce.getCustomer().getId());
                }
            }
        });

        if (ids.isEmpty()) {
            return null;
        }

        Long id = ids.stream().max(Long::compareTo).orElseThrow(() -> new RuntimeException());
        CustomerEvent ce = new CustomerEvent();
        Customer c = new Customer();
        c.setId(id);
        ce.setCustomer(c);
        return ce;
    }

    public void save(CustomerEvent customerEvent) {
        MongoCollection collection = database.getCollection("customer-" + customerEvent.getCustomer().getId().toString());

        Document document = new Document();
        document.put("date", new Date());
        document.put("operation", customerEvent.getOperation());
        document.put("customer", customerEvent.getCustomer());
        document.put("_class", "CustomerEvent");

        collection.insertOne(document);
    }

    public void deleteRepo(Long id) {
        database.getCollection("customer-" + id.toString()).drop();
    }
}
