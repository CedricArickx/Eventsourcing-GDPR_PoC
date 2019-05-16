package com.xti.eventsourcingbackend.service;

import com.xti.eventsourcingbackend.domain.event.Event;
import com.xti.eventsourcingbackend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(String id) {
        return eventRepository.findById(id);
    }
}

