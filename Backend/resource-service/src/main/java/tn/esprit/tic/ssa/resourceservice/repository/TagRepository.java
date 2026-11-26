package tn.esprit.tic.ssa.resourceservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.tic.ssa.resourceservice.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {
}