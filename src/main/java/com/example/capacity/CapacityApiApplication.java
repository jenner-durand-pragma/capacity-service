package com.example.capacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CapacityApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapacityApiApplication.class, args);
	}

}
