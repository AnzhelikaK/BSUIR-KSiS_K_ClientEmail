package com.bsuir.repository.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class SendingMessage {
    private String to;
    private String subject;
    private String text;
    private LocalDateTime date;
}
