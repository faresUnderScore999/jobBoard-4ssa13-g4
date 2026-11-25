package com.example.consultation_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // identifiants Keycloak : pas de clé étrangère vers un autre service
    private String parentId;
    private String childId;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private ConsultationMode mode;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private String reason;

    // plusieurs rendez-vous -> 1 spécialiste
    @ManyToOne
    @JoinColumn(name = "specialist_id")
    private Specialist specialist;

    // 1 rendez-vous -> plusieurs notes de suivi
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FollowUpNote> notes = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    public String getChildId() { return childId; }
    public void setChildId(String childId) { this.childId = childId; }
    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
    public ConsultationMode getMode() { return mode; }
    public void setMode(ConsultationMode mode) { this.mode = mode; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Specialist getSpecialist() { return specialist; }
    public void setSpecialist(Specialist specialist) { this.specialist = specialist; }
    public List<FollowUpNote> getNotes() { return notes; }
    public void setNotes(List<FollowUpNote> notes) { this.notes = notes; }
}
