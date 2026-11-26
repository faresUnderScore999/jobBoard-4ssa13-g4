package tn.esprit.tic.ssa.resourceservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "consultations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceConsultation {
    @Id
    private String id;
    private String resourceId;      // association vers Resource
    private String childId;         // identifiant Keycloak de l'enfant
    private LocalDateTime dateConsultation = LocalDateTime.now();
    private int duration;
}