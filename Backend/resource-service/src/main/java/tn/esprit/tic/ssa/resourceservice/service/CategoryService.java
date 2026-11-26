package tn.esprit.tic.ssa.resourceservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.tic.ssa.resourceservice.model.Category;
import tn.esprit.tic.ssa.resourceservice.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Catégorie introuvable : " + id));
    }

    public Category create(Category category) {
        category.setId(null);
        return repository.save(category);
    }

    public Category update(String id, Category category) {
        findById(id);
        category.setId(id);
        return repository.save(category);
    }

    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }
}