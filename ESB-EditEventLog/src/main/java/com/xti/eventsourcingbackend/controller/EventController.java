package com.xti.eventsourcingbackend.controller;

import com.xti.eventsourcingbackend.domain.event.Event;
import com.xti.eventsourcingbackend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<Event> getAll() {
        return eventService.getAll();
    }

    @GetMapping("/{id}")
    public Event getById(@PathVariable("id") String id) {
        return eventService.getById(id);
    }

    @PostMapping
    public void create(@RequestBody Event event) {
        eventService.create(event);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        eventService.delete(id);
    }
}
