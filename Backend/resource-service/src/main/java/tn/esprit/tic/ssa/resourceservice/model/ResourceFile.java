package tn.esprit.tic.ssa.resourceservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceFile {
    private String id;
    private String fileName;
    private String url;
    private ResourceType type;
    private long size;
}