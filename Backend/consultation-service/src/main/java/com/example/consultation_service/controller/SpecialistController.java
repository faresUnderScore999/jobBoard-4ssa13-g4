package com.example.consultation_service.controller;

import com.example.consultation_service.entity.Availability;
import com.example.consultation_service.entity.Specialist;
import com.example.consultation_service.service.SpecialistService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consultations/specialists")
public class SpecialistController {

    private final SpecialistService specialistService;

    public SpecialistController(SpecialistService specialistService) {
        this.specialistService = specialistService;
    }

    // GET /api/consultations/specialists -> liste des spécialistes (200)
    @GetMapping
    public List<Specialist> list() {
        return specialistService.findAll();
    }

    // GET /api/consultations/specialists/{id} -> un spécialiste (200 ou 404)
    @GetMapping("/{id}")
    public Specialist getById(@PathVariable UUID id) {
        return specialistService.getById(id);
    }

    // POST /api/consultations/specialists -> créer un spécialiste (201)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Specialist create(@RequestBody Specialist specialist) {
        return specialistService.create(specialist);
    }

    // GET /api/consultations/specialists/{id}/availabilities -> ses disponibilités
    @GetMapping("/{id}/availabilities")
    public List<Availability> getAvailabilities(@PathVariable UUID id) {
        return specialistService.getAvailabilities(id);
    }

    // POST /api/consultations/specialists/{id}/availabilities -> ajouter une disponibilité (201)
    @PostMapping("/{id}/availabilities")
    @ResponseStatus(HttpStatus.CREATED)
    public Availability addAvailability(@PathVariable UUID id, @RequestBody Availability availability) {
        return specialistService.addAvailability(id, availability);
    }
}