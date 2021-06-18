package com.publisher.service.publisherservice.publisher;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.batching.BatchingSettings;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.threeten.bp.Duration;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessagePublisher {

	private final MessageConverter messageConverter;
	
	@Value("${app.topic.name}")
	private String topicName;

	public void publishMessage(String message) throws InterruptedException, IOException, JAXBException, ExecutionException {

		Publisher publisher = null;

		try {
			publisher = Publisher.newBuilder(topicName).build();

			PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
					.setData(ByteString.copyFromUtf8(message))
					.build();

			publisher.publish(pubsubMessage);
			//log.info("message published.");
		} finally {
			if (publisher != null) {
				// When finished with the publisher, shutdown to free up resources.
				publisher.shutdown();
				publisher.awaitTermination(1, TimeUnit.MINUTES);
			}
		}
	}

}
