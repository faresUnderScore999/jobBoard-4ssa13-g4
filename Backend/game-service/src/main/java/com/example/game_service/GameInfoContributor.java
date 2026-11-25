package com.example.game_service;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class GameInfoContributor implements InfoContributor {

	@Override
	public void contribute(Info.Builder builder) {
		builder.withDetail("app", java.util.Collections.singletonMap("name", "game-service"))
			.withDetail("description", "Game server microservice")
			.withDetail("version", "0.0.1-SNAPSHOT");
	}

}