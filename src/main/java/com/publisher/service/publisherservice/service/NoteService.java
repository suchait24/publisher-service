package com.publisher.service.publisherservice.service;

import com.publisher.service.publisherservice.dto.NoteDTO;
import com.publisher.service.publisherservice.publisher.MessageConverter;
import com.publisher.service.publisherservice.publisher.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {

    private final MessagePublisher messagePublisher;

    public void produceMessages() throws JAXBException, InterruptedException, ExecutionException, IOException {

        List<String> messagesList = new LinkedList<>();

        int number = 3;
        int defaultPnr = 100;

        for (int i = 0; i < number; i++) {

            for(int j = 1; j < 6; j++) {

                NoteDTO noteDTO = new NoteDTO();
                noteDTO.setTo("Delhi");
                noteDTO.setFrom("India");
                noteDTO.setHeading("Reminder");
                noteDTO.setBody("Body 6");
                noteDTO.setPnrid(defaultPnr + "-"+ j);
                noteDTO.setTimestamp(System.currentTimeMillis());

                messagesList.add(MessageConverter.marshallNoteDTO(noteDTO));
                log.info("message : {}", noteDTO);
            }

            defaultPnr++;
        }

        log.info("Total messages generated : {}", messagesList.size());
        messagePublisher.publishMessage(messagesList);
    }
}
