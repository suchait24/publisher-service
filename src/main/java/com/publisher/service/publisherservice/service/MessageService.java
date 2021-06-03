package com.publisher.service.publisherservice.service;

import com.publisher.service.publisherservice.dto.TeletypeEventDTO;
import com.publisher.service.publisherservice.publisher.MessageConverter;
import com.publisher.service.publisherservice.publisher.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessagePublisher messagePublisher;

    public void processPublishing() throws InterruptedException, ExecutionException, JAXBException, IOException {

        List<String> messageList = new ArrayList<>();
        Random random = new Random();

        byte[] array = new byte[15]; // length is bounded by 7

        int number = 1000;

        for (int i = 0; i < number; i++) {

            Integer messageCorelationId = random.nextInt(10000);

            String carrierCode = generateRandomString(random);
            String hostLocatorCode = generateRandomString(random);

            log.info("messageCorelationId : {}", messageCorelationId);
            log.info("carrierCode : {}", carrierCode);
            log.info("hostlocatorCode : {}", hostLocatorCode);

            TeletypeEventDTO teletypeEventDTO = new TeletypeEventDTO();
            teletypeEventDTO.setCarrierCode(carrierCode);
            teletypeEventDTO.setMessageCorelationId(messageCorelationId.longValue());
            teletypeEventDTO.setHostRecordLocator(hostLocatorCode);

            String message = MessageConverter.marshall(teletypeEventDTO);
            messageList.add(message);
        }

        messagePublisher.publishMessage(messageList);
    }

    private String generateRandomString(Random random) {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();
    }
}
