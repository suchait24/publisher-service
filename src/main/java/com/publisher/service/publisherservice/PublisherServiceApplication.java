package com.publisher.service.publisherservice;

import com.publisher.service.publisherservice.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class PublisherServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PublisherServiceApplication.class, args);
	}

	private final NoteService noteService;

	@Override
	public void run(String... args) throws Exception {
		noteService.produceMessages();
	}
}
