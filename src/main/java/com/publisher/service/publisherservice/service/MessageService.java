package com.publisher.service.publisherservice.service;

import com.publisher.service.publisherservice.dto.AddressLine;
import com.publisher.service.publisherservice.dto.MessageBody;
import com.publisher.service.publisherservice.dto.TeletypeEventDTO;
import com.publisher.service.publisherservice.publisher.MessageConverter;
import com.publisher.service.publisherservice.publisher.MessagePublisher;
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
public class MessageService {

    private final MessagePublisher messagePublisher;

    public void processPublishing() throws InterruptedException, ExecutionException, JAXBException, IOException {

        List<String> messageList = new ArrayList<>();
        Random random = new Random();

        byte[] array = new byte[15]; // length is bounded by 7

        int number = 10;

        for (int i = 0; i < number; i++) {

            Integer messageCorelationId = random.nextInt(10000);

            String resourceLocator = "HDQ1S"+generateRandomString(random);

            String[] messageBodyLine={"TRL",resourceLocator,"HDQEY AAABBC","1CUMBERBATCH/BENEDICTMR","EY042J20DEC DUBAUH HK1","OSI 1S THANK YOU FOR BOOKING ETIHAD"};

            MessageBody messageBody= new MessageBody();
            messageBody.setLine(messageBodyLine);
            AddressLine addressLine = new AddressLine();
            addressLine.setDestination("HDQRM1S");
            addressLine.setPriority("QD");

            TeletypeEventDTO teletypeEventDTO = new TeletypeEventDTO();
            teletypeEventDTO.setOrigin("HDQRMEY");
            teletypeEventDTO.setTimestamp("2019/06/10 09:32");
            teletypeEventDTO.setAddressLine(addressLine);
            teletypeEventDTO.setMessageCorrelationID("SABRE04P-1E95E72-96H5V87QC6BA2ROTQTOLO2M1INA6N7PHG8");
            teletypeEventDTO.setMessageIdentity("100932/38044891");
            teletypeEventDTO.setStandardMessageIdentifier("TRL");
            teletypeEventDTO.setMessageType("TEXT");
            teletypeEventDTO.setMessageBody(messageBody);


            String message = MessageConverter.marshall(teletypeEventDTO);
            messageList.add(message);
        }

        messagePublisher.publishMessageInBatch(messageList,100);
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
