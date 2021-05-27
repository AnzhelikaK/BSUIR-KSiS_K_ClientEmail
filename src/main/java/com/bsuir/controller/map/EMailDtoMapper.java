package com.bsuir.controller.map;

import com.bsuir.controller.dto.EMailDto;
import com.bsuir.repository.entity.EMail;
import javafx.beans.property.SimpleStringProperty;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class EMailDtoMapper {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EMailDto mapFrom(EMail eMail) {
        return EMailDto.builder()
            .date(new SimpleStringProperty(dateTimeFormatter.format(eMail.getDate())))
            .from(new SimpleStringProperty(eMail.getFrom()))
            .subject(new SimpleStringProperty(eMail.getSubject()))
            .text(new SimpleStringProperty(eMail.getText()))
            .build();
    }
}
