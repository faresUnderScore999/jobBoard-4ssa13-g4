package com.example.consultation_service.repository;

import com.example.consultation_service.entity.FollowUpNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FollowUpNoteRepository extends JpaRepository<FollowUpNote, UUID> {

    // notes de suivi d'un rendez-vous
    List<FollowUpNote> findByAppointmentId(UUID appointmentId);
}