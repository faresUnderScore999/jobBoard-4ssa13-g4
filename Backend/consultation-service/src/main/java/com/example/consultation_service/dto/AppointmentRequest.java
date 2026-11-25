package com.example.consultation_service.dto;

import com.example.consultation_service.entity.ConsultationMode;
import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequest(
        UUID specialistId,
        String parentId,
        String childId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        ConsultationMode mode,
        String reason
) {}