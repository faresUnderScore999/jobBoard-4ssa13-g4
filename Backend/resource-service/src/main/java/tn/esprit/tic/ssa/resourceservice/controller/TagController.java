package tn.esprit.tic.ssa.resourceservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.ssa.resourceservice.model.Tag;
import tn.esprit.tic.ssa.resourceservice.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService service;

    @GetMapping
    public List<Tag> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Tag get(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag create(@RequestBody Tag tag) {
        return service.create(tag);
    }

    @PutMapping("/{id}")
    public Tag update(@PathVariable String id, @RequestBody Tag tag) {
        return service.update(id, tag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}