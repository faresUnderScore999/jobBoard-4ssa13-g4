package com.example.event_server.dto;

import java.io.Serializable;

public record RegistrationDto(Long id, Long eventId, String userId, String status) implements Serializable {
}