package com.publisher.service.publisherservice;

import com.publisher.service.publisherservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class PublisherServiceApplication implements CommandLineRunner {

	private final MessageService messageService;

	public static void main(String[] args) {
		SpringApplication.run(PublisherServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		messageService.processPublishing();
	}
}
