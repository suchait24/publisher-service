package com.publisher.service.publisherservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@XmlRootElement(name = "note")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"to", "from", "heading","body","pnrid","timestamp"})
public class NoteDTO {

    @XmlElement(name = "to")
    private String to;

    @XmlElement(name = "from")
    private String from;

    @XmlElement(name = "heading")
    private String heading;

    @XmlElement(name = "body")
    private String body;

    @XmlElement(name = "pnrid")
    private String pnrid;

    @XmlElement(name = "timestamp")
    private Long timestamp;
}
