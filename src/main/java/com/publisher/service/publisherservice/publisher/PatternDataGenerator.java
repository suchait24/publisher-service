package com.publisher.service.publisherservice.publisher;

import com.publisher.service.publisherservice.dto.TeletypeEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatternDataGenerator {

    private final MessagePublisher messagePublisher;

    public void generateData() throws JAXBException, InterruptedException, ExecutionException, IOException {

        Random random = new Random();

        byte[] array = new byte[15]; // length is bounded by 7

        Map<Integer, Integer> dataSet = new HashMap<>();
        dataSet.put(1, 200);
        dataSet.put(2, 50);
        dataSet.put(3, 300);
        dataSet.put(4, 100);
        dataSet.put(5, 1000);
        dataSet.put(6, 700);
        dataSet.put(7, 600);
        dataSet.put(8, 500);
        dataSet.put(9, 850);
        dataSet.put(10, 700);

        for(int i = 0; i < 20; i++) {

            for(int j = 1; j <= 10; j++) {

                List<String> msgsList = new ArrayList<>();
                long batchSize = dataSet.get(j);

                for(int k = 0; k < batchSize; k++) {
                    //create messages and publish to topic in batch

                    Integer messageCorelationId = random.nextInt(10000);

                    String carrierCode = generateRandomString(random);
                    String hostLocatorCode = generateRandomString(random);

                    //log.info("messageCorelationId : {}", messageCorelationId);
                    //log.info("carrierCode : {}", carrierCode);
                    //log.info("hostlocatorCode : {}", hostLocatorCode);

                    TeletypeEventDTO teletypeEventDTO = new TeletypeEventDTO();
                    teletypeEventDTO.setCarrierCode(carrierCode);
                    teletypeEventDTO.setMessageCorelationId(messageCorelationId.longValue());
                    teletypeEventDTO.setHostRecordLocator(hostLocatorCode);

                    String message = MessageConverter.marshall(teletypeEventDTO);
                    msgsList.add(message);
                }

                //batch call here
                messagePublisher.publishMessage(msgsList,batchSize);

            }
        }

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
