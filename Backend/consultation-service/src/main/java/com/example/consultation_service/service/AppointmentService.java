package com.example.consultation_service.service;

import com.example.consultation_service.dto.AppointmentRequest;
import com.example.consultation_service.entity.Appointment;
import com.example.consultation_service.entity.AppointmentStatus;
import com.example.consultation_service.entity.FollowUpNote;
import com.example.consultation_service.repository.AppointmentRepository;
import com.example.consultation_service.repository.FollowUpNoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final FollowUpNoteRepository noteRepository;
    private final SpecialistService specialistService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              FollowUpNoteRepository noteRepository,
                              SpecialistService specialistService) {
        this.appointmentRepository = appointmentRepository;
        this.noteRepository = noteRepository;
        this.specialistService = specialistService;
    }

    // READ : tous les rendez-vous, ou ceux d'un enfant
    public List<Appointment> findAll(String childId) {
        if (childId != null) {
            return appointmentRepository.findByChildId(childId);
        }
        return appointmentRepository.findAll();
    }

    // READ : un seul rendez-vous
    public Appointment getById(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rendez-vous introuvable"));
    }

    // CREATE : prendre un rendez-vous
    public Appointment book(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        fill(appointment, request);
        appointment.setStatus(AppointmentStatus.EN_ATTENTE); // un nouveau RDV est toujours en attente
        return appointmentRepository.save(appointment);
    }

    // UPDATE : modifier un rendez-vous
    public Appointment update(UUID id, AppointmentRequest request) {
        Appointment appointment = getById(id);
        fill(appointment, request);
        return appointmentRepository.save(appointment);
    }

    // UPDATE du statut : confirmer, terminer, annuler
    public Appointment changeStatus(UUID id, AppointmentStatus status) {
        Appointment appointment = getById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    // DELETE : supprimer un rendez-vous
    public void delete(UUID id) {
        appointmentRepository.delete(getById(id));
    }

    // notes de suivi
    public FollowUpNote addNote(UUID appointmentId, FollowUpNote note) {
        note.setAppointment(getById(appointmentId));   // lien note -> rendez-vous
        return noteRepository.save(note);
    }

    public List<FollowUpNote> getNotes(UUID appointmentId) {
        getById(appointmentId);   // 404 si le rendez-vous n'existe pas
        return noteRepository.findByAppointmentId(appointmentId);
    }

    // copie les données reçues dans l'entité
    private void fill(Appointment appointment, AppointmentRequest request) {
        appointment.setSpecialist(specialistService.getById(request.specialistId()));
        appointment.setParentId(request.parentId());
        appointment.setChildId(request.childId());
        appointment.setStartAt(request.startAt());
        appointment.setEndAt(request.endAt());
        appointment.setMode(request.mode());
        appointment.setReason(request.reason());
    }
}