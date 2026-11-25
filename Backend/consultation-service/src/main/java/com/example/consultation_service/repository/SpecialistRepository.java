package com.example.consultation_service.repository;

import com.example.consultation_service.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpecialistRepository extends JpaRepository<Specialist, UUID> {
}