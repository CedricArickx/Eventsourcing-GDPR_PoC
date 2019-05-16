package com.xti.eventsourcingbackend.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.xti.eventsourcingbackend.domain.event.Event;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Repository
public class EventRepository {

    private MongoDatabase database;

    public EventRepository() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClient mongoClient = new MongoClient();
        database = mongoClient.getDatabase("eventsourcing").withCodecRegistry(pojoCodecRegistry);
    }

    public List<Event> findAll() {
        List<Event> result = new ArrayList<>();
        database.listCollectionNames().forEach((Consumer<? super String>) n -> {
            database.getCollection(n, Event.class).find().forEach((Consumer<? super Event>) m -> {
                result.add(m);
            });
        });

        return result;
    }

    public Event findById(String id) {
        return findAll().stream().filter(n -> n.getId() == id).findFirst().orElse(null);
    }
}
