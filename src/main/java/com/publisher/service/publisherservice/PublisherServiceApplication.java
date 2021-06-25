package com.publisher.service.publisherservice;

import com.publisher.service.publisherservice.publisher.PatternDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class PublisherServiceApplication implements CommandLineRunner {

	private final PatternDataGenerator patternDataGenerator;

	public static void main(String[] args) {
		SpringApplication.run(PublisherServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//patternDataGenerator.generateData();
	}
}
