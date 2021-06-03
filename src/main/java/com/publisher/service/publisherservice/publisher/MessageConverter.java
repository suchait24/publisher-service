package com.publisher.service.publisherservice.publisher;

import com.publisher.service.publisherservice.dto.TeletypeEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

@Slf4j
@Component
public class MessageConverter {

	public static TeletypeEventDTO unmarshall(String message) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(TeletypeEventDTO.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		TeletypeEventDTO teletypeEventDTO = (TeletypeEventDTO) unmarshaller.unmarshal(new StringReader(message));

		log.info("Teletype dto generated : {}", teletypeEventDTO);

		return teletypeEventDTO;
	}

	public static String marshall(TeletypeEventDTO teletypeEventDTO) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(TeletypeEventDTO.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(teletypeEventDTO, stringWriter);

		String result = stringWriter.toString();
		log.info("Teletype XML generated : {}", result);

		return result;
	}

}