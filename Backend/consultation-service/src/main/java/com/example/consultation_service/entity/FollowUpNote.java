package com.example.consultation_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class FollowUpNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String authorId;
    private String content;
    private boolean visibleToParent;

    // plusieurs notes -> 1 rendez-vous
    @ManyToOne
    @JoinColumn(name = "appointment_id")
    @JsonIgnore
    private Appointment appointment;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public boolean isVisibleToParent() { return visibleToParent; }
    public void setVisibleToParent(boolean visibleToParent) { this.visibleToParent = visibleToParent; }
    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
}
