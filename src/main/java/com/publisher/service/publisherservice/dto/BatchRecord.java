package com.publisher.service.publisherservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BatchRecord {

    private List<com.publisher.service.publisherservice.dto.TeletypeEventDTO> dtoList;
    private Integer batchMessageId;
    private LocalDateTime batchReceivedTime;
}
