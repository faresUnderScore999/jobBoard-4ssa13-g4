package com.example.event_server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EventController {

	@GetMapping("/ping")
	public String ping() {
		return "event-server is up";
	}

}