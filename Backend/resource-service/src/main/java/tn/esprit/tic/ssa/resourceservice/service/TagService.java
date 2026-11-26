package tn.esprit.tic.ssa.resourceservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.tic.ssa.resourceservice.model.Tag;
import tn.esprit.tic.ssa.resourceservice.repository.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository repository;

    public List<Tag> findAll() {
        return repository.findAll();
    }

    public Tag findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tag introuvable : " + id));
    }

    public Tag create(Tag tag) {
        tag.setId(null);
        return repository.save(tag);
    }

    public Tag update(String id, Tag tag) {
        findById(id);
        tag.setId(id);
        return repository.save(tag);
    }

    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }
}