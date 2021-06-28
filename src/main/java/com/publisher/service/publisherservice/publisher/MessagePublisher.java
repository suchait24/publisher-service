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
import java.nio.charset.StandardCharsets;
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

	public void publishMessage(List<String> messageList) throws InterruptedException, IOException, JAXBException, ExecutionException {

		Publisher publisher = Publisher.newBuilder(topicName).setEnableMessageOrdering(true).build();

		messageList.forEach(message -> {
			ByteString data = ByteString.copyFromUtf8(message);
			try {
				PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).setOrderingKey(MessageConverter.unmarshallNoteDTO(message).getPnrid()).build();
				publisher.publish(pubsubMessage);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		});

		/*
		Publisher publisher = null;
		List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

		try {
			publisher = Publisher.newBuilder(topicName).setEnableMessageOrdering(true).build();

			// schedule publishing one message at a time : messages get automatically batched
			Publisher finalPublisher = publisher;
			messageList.forEach(message -> {
				ByteString data = ByteString.copyFromUtf8(message);
				PubsubMessage pubsubMessage = null;
				try {
					pubsubMessage = PubsubMessage.newBuilder().setData(data).setOrderingKey(MessageConverter.unmarshallNoteDTO(message).getPnrid()).build();
				} catch (JAXBException e) {
					e.printStackTrace();
				}

				// Once published, returns a server-assigned message id (unique within the topic)
				ApiFuture<String> messageIdFuture = finalPublisher.publish(pubsubMessage);
				messageIdFutures.add(messageIdFuture);
			});
		} finally {
			// Wait on any pending publish requests.
			List<String> messageIds = ApiFutures.allAsList(messageIdFutures).get();

			System.out.println("Published " + messageIds.size() + " messages with batch settings.");

			if (publisher != null) {
				// When finished with the publisher, shutdown to free up resources.
				publisher.shutdown();
				publisher.awaitTermination(1, TimeUnit.MINUTES);
			}
		}

		 */
	}

}
