package com.example.event_server.dto;

import java.io.Serializable;

public record EventTypeDto(Long id, String name) implements Serializable {
}