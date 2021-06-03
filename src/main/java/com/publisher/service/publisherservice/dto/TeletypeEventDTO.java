package com.publisher.service.publisherservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@ToString
@XmlRootElement(name = "TeletypEvent")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"messageCorelationId", "carrierCode", "hostRecordLocator"})
public class TeletypeEventDTO {

    @XmlElement(name = "messageCorelationId")
    private Long messageCorelationId;

    @XmlElement(name = "carrierCode")
    private String carrierCode;

    @XmlElement(name = "hostRecordLocator")
    private String hostRecordLocator;
}
