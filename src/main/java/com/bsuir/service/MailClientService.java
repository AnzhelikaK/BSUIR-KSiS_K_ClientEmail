package com.bsuir.service;

import com.bsuir.repository.entity.EMail;
import com.bsuir.repository.entity.SendingMessage;
import com.bsuir.repository.entity.UserCredentials;

import java.util.Collection;

public interface MailClientService {

    Collection<EMail> receiveEmails(UserCredentials userCredentials);

    void sendEmail(UserCredentials credentials, SendingMessage message);
}
