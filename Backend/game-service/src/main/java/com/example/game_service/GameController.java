package com.example.game_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

	@GetMapping("/ping")
	public String ping() {
		return "game-service is up";
	}

}