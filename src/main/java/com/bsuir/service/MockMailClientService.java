package com.bsuir.service;

import com.bsuir.repository.entity.EMail;
import com.bsuir.repository.entity.SendingMessage;
import com.bsuir.repository.entity.UserCredentials;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class MockMailClientService implements MailClientService {

    @Override
    public Collection<EMail> receiveEmails(UserCredentials userCredentials) {
        return List.of(
            EMail.builder()
                .from("Hleb")
                .text("It works\nasfd\nssdfadsf\nasdf\nxcv\nalksdfjlk\nsdfasdf\nasdfd\nasdfsaf\nljzxcklvjzxclv")
                .date(LocalDateTime.now())
                .build(),
            EMail.builder()
                .from("Den")
                .text("Den message")
                .date(LocalDateTime.now())
                .build(),
            EMail.builder()
                .from("Alex")
                .text("messagsdgsag Alex")
                .date(LocalDateTime.now())
                .build(),
            EMail.builder()
                .from("Bob")
                .text("Bbbbboboooob")
                .date(LocalDateTime.now())
                .build()
        );
    }

    @Override
    public void sendEmail(UserCredentials credentials, SendingMessage message) {

    }
}
