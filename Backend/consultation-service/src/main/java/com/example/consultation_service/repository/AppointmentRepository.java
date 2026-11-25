package com.example.consultation_service.repository;

import com.example.consultation_service.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // historique des rendez-vous d'un enfant : GET /appointments?childId=
    List<Appointment> findByChildId(String childId);
}