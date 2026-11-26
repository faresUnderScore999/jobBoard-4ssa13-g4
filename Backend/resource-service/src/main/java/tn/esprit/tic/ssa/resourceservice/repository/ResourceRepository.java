package tn.esprit.tic.ssa.resourceservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.tic.ssa.resourceservice.model.Resource;
import tn.esprit.tic.ssa.resourceservice.model.ResourceType;

import java.util.List;

public interface ResourceRepository extends MongoRepository<Resource, String> {
    List<Resource> findByCategoryId(String categoryId);
    List<Resource> findByType(ResourceType type);
    List<Resource> findByTitleContainingIgnoreCase(String title);
}