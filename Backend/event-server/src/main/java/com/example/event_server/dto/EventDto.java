package com.example.event_server.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EventDto(Long id, String title, LocalDateTime startAt, String location, int capacity, Long eventTypeId) implements Serializable {
}