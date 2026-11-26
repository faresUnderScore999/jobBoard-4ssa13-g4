package tn.esprit.tic.ssa.resourceservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.ssa.resourceservice.model.Resource;
import tn.esprit.tic.ssa.resourceservice.model.ResourceType;
import tn.esprit.tic.ssa.resourceservice.service.ResourceService;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @GetMapping
    public List<Resource> list(@RequestParam(required = false) String category,
                               @RequestParam(required = false) ResourceType type,
                               @RequestParam(required = false) String title) {
        return service.findAll(category, type, title);
    }

    @GetMapping("/{id}")
    public Resource get(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resource create(@RequestBody Resource resource) {
        return service.create(resource);
    }

    @PutMapping("/{id}")
    public Resource update(@PathVariable String id, @RequestBody Resource resource) {
        return service.update(id, resource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}