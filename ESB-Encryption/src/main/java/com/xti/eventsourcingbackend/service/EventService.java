package com.xti.eventsourcingbackend.service;

import com.xti.eventsourcingbackend.domain.event.Event;
import com.xti.eventsourcingbackend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAll() {
        return StreamSupport.stream(eventRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Event getById(String id) {
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public void create(Event event) {
        eventRepository.save(event);
    }

    public void delete(String id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException());
        eventRepository.delete(event);
    }
}
