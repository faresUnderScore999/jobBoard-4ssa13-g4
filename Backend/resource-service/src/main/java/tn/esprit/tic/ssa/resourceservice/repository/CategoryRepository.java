package tn.esprit.tic.ssa.resourceservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.tic.ssa.resourceservice.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
}