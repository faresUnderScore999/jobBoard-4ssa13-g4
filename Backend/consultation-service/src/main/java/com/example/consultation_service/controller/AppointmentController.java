package com.example.consultation_service.controller;

import com.example.consultation_service.dto.AppointmentRequest;
import com.example.consultation_service.dto.StatusRequest;
import com.example.consultation_service.entity.Appointment;
import com.example.consultation_service.entity.FollowUpNote;
import com.example.consultation_service.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consultations/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // CREATE - POST /api/consultations/appointments -> prendre un RDV (201)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Appointment book(@RequestBody AppointmentRequest request) {
        return appointmentService.book(request);
    }

    // READ - GET /api/consultations/appointments?childId=... -> historique (200)
    @GetMapping
    public List<Appointment> list(@RequestParam(required = false) String childId) {
        return appointmentService.findAll(childId);
    }

    // READ - GET /api/consultations/appointments/{id} -> un RDV (200 ou 404)
    @GetMapping("/{id}")
    public Appointment getById(@PathVariable UUID id) {
        return appointmentService.getById(id);
    }

    // UPDATE - PUT /api/consultations/appointments/{id} -> modifier un RDV (200 ou 404)
    @PutMapping("/{id}")
    public Appointment update(@PathVariable UUID id, @RequestBody AppointmentRequest request) {
        return appointmentService.update(id, request);
    }

    // UPDATE - PATCH /api/consultations/appointments/{id}/status -> confirmer / terminer / annuler
    @PatchMapping("/{id}/status")
    public Appointment changeStatus(@PathVariable UUID id, @RequestBody StatusRequest request) {
        return appointmentService.changeStatus(id, request.status());
    }

    // DELETE - DELETE /api/consultations/appointments/{id} -> supprimer (204 ou 404)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        appointmentService.delete(id);
    }

    // POST /api/consultations/appointments/{id}/notes -> ajouter une note de suivi (201)
    @PostMapping("/{id}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public FollowUpNote addNote(@PathVariable UUID id, @RequestBody FollowUpNote note) {
        return appointmentService.addNote(id, note);
    }

    // GET /api/consultations/appointments/{id}/notes -> notes d'un RDV
    @GetMapping("/{id}/notes")
    public List<FollowUpNote> getNotes(@PathVariable UUID id) {
        return appointmentService.getNotes(id);
    }
}