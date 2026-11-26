package com.example.communication_service;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pictograms")
public class PictogramController {

    @GetMapping
    public List<String> all() {
        return List.of("manger", "boire", "jouer");
    }
}