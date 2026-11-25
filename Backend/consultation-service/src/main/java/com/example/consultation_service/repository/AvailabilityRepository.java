package com.example.consultation_service.repository;

import com.example.consultation_service.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {
}