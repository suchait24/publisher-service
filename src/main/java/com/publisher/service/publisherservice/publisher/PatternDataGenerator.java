package com.publisher.service.publisherservice.publisher;

import com.publisher.service.publisherservice.dto.AddressLine;
import com.publisher.service.publisherservice.dto.MessageBody;
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

        Integer pos = 0;

        for(int i = 0; i < 1; i++) {

            for(int j = 1; j <= 1; j++) {

                List<String> msgsList = new ArrayList<>();
                long batchSize = dataSet.get(j);

                for(int k = 0; k < batchSize; k++) {
                    //create messages and publish to topic in batch

                    Integer messageCorelationId = random.nextInt(10000);

                    String resourceLocator = "HDQ1S"+generateRandomString(random);

                    String[] messageBodyLine={"TRL",resourceLocator,"HDQEY AAABBC","1CUMBERBATCH/BENEDICTMR","EY042J20DEC DUBAUH HK1","OSI 1S THANK YOU FOR BOOKING ETIHAD"};

                    MessageBody messageBody= new MessageBody();
                    messageBody.setLine(messageBodyLine);
                    AddressLine addressLine = new AddressLine();
                    addressLine.setDestination("HDQRM1S");
                    addressLine.setPriority("QD");

                    TeletypeEventDTO teletypeEventDTO = new TeletypeEventDTO();
                    teletypeEventDTO.setOrigin("HDQRMEY" + pos++);
                    teletypeEventDTO.setTimestamp("2019/06/10 09:32");
                    teletypeEventDTO.setAddressLine(addressLine);
                    teletypeEventDTO.setMessageCorrelationID("SABRE04P-1E95E72-96H5V87QC6BA2ROTQTOLO2M1INA6N7PHG8");
                    teletypeEventDTO.setMessageIdentity("100932/38044891");
                    teletypeEventDTO.setStandardMessageIdentifier("TRL");
                    teletypeEventDTO.setMessageType("TEXT");
                    teletypeEventDTO.setMessageBody(messageBody);


                    String message = MessageConverter.marshall(teletypeEventDTO);
                    msgsList.add(message);
                }

                //batch call here
                messagePublisher.publish(msgsList);

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

