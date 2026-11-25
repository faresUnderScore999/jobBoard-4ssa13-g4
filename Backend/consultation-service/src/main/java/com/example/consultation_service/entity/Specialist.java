package com.example.consultation_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class Specialist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String keycloakUserId;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    // modes de consultation proposés : EN_LIGNE et/ou PRESENTIEL
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<ConsultationMode> modes = new HashSet<>();

    // 1 spécialiste -> plusieurs disponibilités
    @OneToMany(mappedBy = "specialist", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Availability> availabilities = new ArrayList<>();

    // 1 spécialiste -> plusieurs rendez-vous
    @OneToMany(mappedBy = "specialist")
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Specialty getSpecialty() { return specialty; }
    public void setSpecialty(Specialty specialty) { this.specialty = specialty; }
    public Set<ConsultationMode> getModes() { return modes; }
    public void setModes(Set<ConsultationMode> modes) { this.modes = modes; }
    public List<Availability> getAvailabilities() { return availabilities; }
    public void setAvailabilities(List<Availability> availabilities) { this.availabilities = availabilities; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}
