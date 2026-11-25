package com.example.consultation_service.service;

import com.example.consultation_service.entity.Availability;
import com.example.consultation_service.entity.Specialist;
import com.example.consultation_service.repository.AvailabilityRepository;
import com.example.consultation_service.repository.SpecialistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class SpecialistService {

    private final SpecialistRepository specialistRepository;
    private final AvailabilityRepository availabilityRepository;

    public SpecialistService(SpecialistRepository specialistRepository,
                             AvailabilityRepository availabilityRepository) {
        this.specialistRepository = specialistRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public List<Specialist> findAll() {
        return specialistRepository.findAll();
    }

    public Specialist getById(UUID id) {
        return specialistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Spécialiste introuvable"));
    }

    public Specialist create(Specialist specialist) {
        return specialistRepository.save(specialist);
    }

    public List<Availability> getAvailabilities(UUID specialistId) {
        return getById(specialistId).getAvailabilities();
    }

    public Availability addAvailability(UUID specialistId, Availability availability) {
        Specialist specialist = getById(specialistId);
        availability.setSpecialist(specialist);   // on crée le lien avec le spécialiste
        return availabilityRepository.save(availability);
    }
}