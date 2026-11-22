package com.example.event_server;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class EventInfoContributor implements InfoContributor {

	@Override
	public void contribute(Info.Builder builder) {
		builder.withDetail("app", java.util.Collections.singletonMap("name", "event-server"))
				.withDetail("description", "Event server microservice")
				.withDetail("version", "0.0.1-SNAPSHOT");
	}

}