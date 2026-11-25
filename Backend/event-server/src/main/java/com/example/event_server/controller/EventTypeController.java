package com.example.event_server.controller;

import com.example.event_server.dto.EventDto;
import com.example.event_server.dto.EventTypeDto;
import com.example.event_server.entity.EventType;
import com.example.event_server.repository.EventRepository;
import com.example.event_server.repository.EventTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-types")
public class EventTypeController {

    private final EventTypeRepository eventTypeRepository;
    private final EventRepository eventRepository;

    public EventTypeController(EventTypeRepository eventTypeRepository, EventRepository eventRepository) {
        this.eventTypeRepository = eventTypeRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<EventTypeDto> list() {
        return eventTypeRepository.findAll().stream()
                .map(et -> new EventTypeDto(et.getId(), et.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventTypeDto> getById(@PathVariable Long id) {
        return eventTypeRepository.findById(id)
                .map(et -> ResponseEntity.ok(new EventTypeDto(et.getId(), et.getName())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventTypeDto> create(@RequestBody EventTypeDto dto) {
        EventType et = new EventType();
        et.setName(dto.name());
        EventType saved = eventTypeRepository.save(et);
        return ResponseEntity.created(URI.create("/api/event-types/" + saved.getId()))
                .body(new EventTypeDto(saved.getId(), saved.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventTypeDto> update(@PathVariable Long id, @RequestBody EventTypeDto dto) {
        return eventTypeRepository.findById(id)
                .map(et -> {
                    et.setName(dto.name());
                    EventType saved = eventTypeRepository.save(et);
                    return ResponseEntity.ok(new EventTypeDto(saved.getId(), saved.getName()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!eventTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventTypeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/events")
    public List<EventDto> getEvents(@PathVariable Long id) {
        if (!eventTypeRepository.existsById(id)) {
            throw new RuntimeException("EventType not found");
        }
        return eventRepository.findByEventTypeId(id).stream()
                .map(e -> new EventDto(e.getId(), e.getTitle(), e.getStartAt(), e.getLocation(), e.getCapacity(), e.getEventTypeId()))
                .collect(Collectors.toList());
    }
}