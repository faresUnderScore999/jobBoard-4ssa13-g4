package tn.esprit.tic.ssa.resourceservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    private String id;
    private String title;
    private String description;
    private ResourceType type;
    private Level level;
    private int ageMin;
    private int ageMax;
    private LocalDateTime createdAt = LocalDateTime.now();

    private String categoryId;                          // association vers Category
    private List<String> tagIds = new ArrayList<>();    // association vers Tag (plusieurs)
    private List<ResourceFile> files = new ArrayList<>(); // fichiers embarqués
}