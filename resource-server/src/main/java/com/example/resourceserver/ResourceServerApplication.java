package com.example.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.example.resourceserver.config")
public class ResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}

}
