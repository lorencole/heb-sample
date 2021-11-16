package com.l.heb.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfiguration extends ResourceConfig {

	public JerseyConfiguration() {
		this.packages("com.l.heb.controllers");
	}
	
}
