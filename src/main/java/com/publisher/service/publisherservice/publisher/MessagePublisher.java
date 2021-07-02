package com.publisher.service.publisherservice.publisher;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.batching.BatchingSettings;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.collect.ImmutableMap;
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

	public void publish(List<String> messageList) throws IOException, JAXBException {

		Publisher publisher = Publisher.newBuilder(topicName).setEnableMessageOrdering(true).build();

		for(String message : messageList) {
			ByteString data = ByteString.copyFromUtf8(message);
			PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
					.setData(data)
					//.setOrderingKey(MessageConverter.unmarshall(message).getOrigin())
					.putAllAttributes(ImmutableMap.of("orderingKey", "Origin")).setOrderingKey("Origin")
					.build();
			log.info("Message : {}", pubsubMessage);
			publisher.publish(pubsubMessage);
		}
	}

	public void publishMessageInBatch(List<String> messageList, long messageCountBatchSize) throws InterruptedException, IOException, JAXBException, ExecutionException {

		Publisher publisher = null;
		List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

		try {
			// Batch settings control how the publisher batches messages
			long requestBytesThreshold = 5000L; // default : 1 byte
			Duration publishDelayThreshold = Duration.ofMillis(100); // default : 1 ms

			// Publish request get triggered based on request size, messages count & time since last
			// publish, whichever condition is met first.
			BatchingSettings batchingSettings =
					BatchingSettings.newBuilder()
							.setElementCountThreshold(messageCountBatchSize)
							.setRequestByteThreshold(requestBytesThreshold)
							.setDelayThreshold(publishDelayThreshold)
							.build();

			// Create a publisher instance with default settings bound to the topic
			publisher = Publisher.newBuilder(topicName).setBatchingSettings(batchingSettings).build();

			// schedule publishing one message at a time : messages get automatically batched
			Publisher finalPublisher = publisher;
			messageList.forEach(message -> {
				ByteString data = ByteString.copyFromUtf8(message);
				PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

				// Once published, returns a server-assigned message id (unique within the topic)
				ApiFuture<String> messageIdFuture = finalPublisher.publish(pubsubMessage);
				messageIdFutures.add(messageIdFuture);
			});
		} finally {
			// Wait on any pending publish requests.
			List<String> messageIds = ApiFutures.allAsList(messageIdFutures).get();

			log.info("Published " + messageIds.size() + " messages with batch settings.");

			if (publisher != null) {
				// When finished with the publisher, shutdown to free up resources.
				publisher.shutdown();
				publisher.awaitTermination(1, TimeUnit.MINUTES);
			}
		}
	}

}
