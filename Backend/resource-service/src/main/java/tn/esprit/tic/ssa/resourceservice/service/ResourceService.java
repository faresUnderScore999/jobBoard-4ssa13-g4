package tn.esprit.tic.ssa.resourceservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.tic.ssa.resourceservice.model.Resource;
import tn.esprit.tic.ssa.resourceservice.model.ResourceType;
import tn.esprit.tic.ssa.resourceservice.repository.CategoryRepository;
import tn.esprit.tic.ssa.resourceservice.repository.ResourceRepository;
import tn.esprit.tic.ssa.resourceservice.repository.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository repository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public List<Resource> findAll(String categoryId, ResourceType type, String title) {
        if (categoryId != null) return repository.findByCategoryId(categoryId);
        if (type != null) return repository.findByType(type);
        if (title != null) return repository.findByTitleContainingIgnoreCase(title);
        return repository.findAll();
    }

    public Resource findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ressource introuvable : " + id));
    }

    public Resource create(Resource resource) {
        checkReferences(resource);   // vérifie que la catégorie et les tags existent
        resource.setId(null);        // MongoDB génère l'id
        return repository.save(resource);
    }

    public Resource update(String id, Resource resource) {
        findById(id);                // lance 404 si elle n'existe pas
        checkReferences(resource);
        resource.setId(id);
        return repository.save(resource);
    }

    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }

    // Vérifie que categoryId et tagIds pointent vers des documents existants
    private void checkReferences(Resource resource) {
        if (resource.getCategoryId() != null
                && !categoryRepository.existsById(resource.getCategoryId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Catégorie inexistante : " + resource.getCategoryId());
        }
        if (resource.getTagIds() != null) {
            for (String tagId : resource.getTagIds()) {
                if (!tagRepository.existsById(tagId)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Tag inexistant : " + tagId);
                }
            }
        }
    }
}