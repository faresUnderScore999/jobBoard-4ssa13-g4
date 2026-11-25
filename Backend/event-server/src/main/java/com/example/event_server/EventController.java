package com.example.event_server;

import com.example.event_server.dto.EventDto;
import com.example.event_server.dto.RegistrationDto;
import com.example.event_server.entity.Event;
import com.example.event_server.repository.EventRepository;
import com.example.event_server.repository.RegistrationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EventController {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventController(EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @GetMapping("/ping")
    public String ping() {
        return "event-server is up";
    }

    @GetMapping("/events")
    public List<EventDto> listEvents() {
        return eventRepository.findAll().stream()
                .map(e -> new EventDto(e.getId(), e.getTitle(), e.getStartAt(), e.getLocation(), e.getCapacity(), e.getEventTypeId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(e -> ResponseEntity.ok(new EventDto(e.getId(), e.getTitle(), e.getStartAt(), e.getLocation(), e.getCapacity(), e.getEventTypeId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/events")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto dto) {
        Event e = new Event();
        e.setTitle(dto.title());
        e.setStartAt(dto.startAt());
        e.setLocation(dto.location());
        e.setCapacity(dto.capacity());
        Event saved = eventRepository.save(e);
        return ResponseEntity.created(URI.create("/api/events/" + saved.getId()))
                .body(new EventDto(saved.getId(), saved.getTitle(), saved.getStartAt(), saved.getLocation(), saved.getCapacity(), saved.getEventTypeId()));
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @RequestBody EventDto dto) {
        return eventRepository.findById(id)
                .map(e -> {
                    e.setTitle(dto.title());
                    e.setStartAt(dto.startAt());
                    e.setLocation(dto.location());
                    e.setCapacity(dto.capacity());
                    Event saved = eventRepository.save(e);
                    return ResponseEntity.ok(new EventDto(saved.getId(), saved.getTitle(), saved.getStartAt(), saved.getLocation(), saved.getCapacity(), saved.getEventTypeId()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/events/{id}/registrations")
    public List<RegistrationDto> getEventRegistrations(@PathVariable Long id) {
        return registrationRepository.findByEventId(id).stream()
                .map(r -> new RegistrationDto(r.getId(), r.getEventId(), r.getUserId(), r.getStatus()))
                .collect(Collectors.toList());
    }
}