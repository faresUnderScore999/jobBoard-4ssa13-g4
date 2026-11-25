package com.example.event_server.controller;

import com.example.event_server.dto.EventDto;
import com.example.event_server.dto.RegistrationDto;
import com.example.event_server.entity.Event;
import com.example.event_server.entity.Registration;
import com.example.event_server.repository.EventRepository;
import com.example.event_server.repository.RegistrationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;

    public RegistrationController(RegistrationRepository registrationRepository, EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<RegistrationDto> list() {
        return registrationRepository.findAll().stream()
                .map(r -> new RegistrationDto(r.getId(), r.getEventId(), r.getUserId(), r.getStatus()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationDto> getById(@PathVariable Long id) {
        return registrationRepository.findById(id)
                .map(r -> ResponseEntity.ok(new RegistrationDto(r.getId(), r.getEventId(), r.getUserId(), r.getStatus())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RegistrationDto> create(@RequestBody RegistrationDto dto) {
        Event event = eventRepository.findById(dto.eventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Registration r = new Registration();
        r.setEvent(event);
        r.setEventId(dto.eventId());
        r.setUserId(dto.userId());
        r.setStatus(dto.status());
        Registration saved = registrationRepository.save(r);
        return ResponseEntity.created(URI.create("/api/registrations/" + saved.getId()))
                .body(new RegistrationDto(saved.getId(), saved.getEventId(), saved.getUserId(), saved.getStatus()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistrationDto> update(@PathVariable Long id, @RequestBody RegistrationDto dto) {
        return registrationRepository.findById(id)
                .map(r -> {
                    if (dto.eventId() != null) {
                        Event event = eventRepository.findById(dto.eventId())
                                .orElseThrow(() -> new RuntimeException("Event not found"));
                        r.setEvent(event);
                        r.setEventId(dto.eventId());
                    }
                    r.setUserId(dto.userId());
                    r.setStatus(dto.status());
                    Registration saved = registrationRepository.save(r);
                    return ResponseEntity.ok(new RegistrationDto(saved.getId(), saved.getEventId(), saved.getUserId(), saved.getStatus()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!registrationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        registrationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/events/{eventId}")
    public List<RegistrationDto> getByEvent(@PathVariable Long eventId) {
        return registrationRepository.findByEventId(eventId).stream()
                .map(r -> new RegistrationDto(r.getId(), r.getEventId(), r.getUserId(), r.getStatus()))
                .collect(Collectors.toList());
    }
}