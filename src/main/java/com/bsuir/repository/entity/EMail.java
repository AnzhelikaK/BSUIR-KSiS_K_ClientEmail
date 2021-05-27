package com.bsuir.repository.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class EMail {
    private final String subject;
    private final String text;
    private final String from;
    private final LocalDateTime date;
}
