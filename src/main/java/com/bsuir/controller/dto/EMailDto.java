package com.bsuir.controller.dto;

import javafx.beans.property.SimpleStringProperty;
import lombok.Builder;

@Builder
public class EMailDto {
    private final SimpleStringProperty date;
    private final SimpleStringProperty from;
    private final SimpleStringProperty subject;
    private final SimpleStringProperty text;

    public String getDate() {
        return date.get();
    }

    public String getFrom() {
        return from.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public String getText() {
        return text.get();
    }
}
