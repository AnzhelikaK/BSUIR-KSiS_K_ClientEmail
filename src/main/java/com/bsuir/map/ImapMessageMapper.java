package com.bsuir.map;

import com.bsuir.repository.entity.EMail;
import com.sun.mail.imap.IMAPMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ImapMessageMapper {

    public EMail mapToEmail(IMAPMessage message) {
        try {
            return EMail.builder()
                .from(getAddress(message))
                .subject(getSubject(message))
                .date(getDate(message))
                .text(getTextFromMessage(message))
                .build();
        } catch (Exception e) {
            log.error("Can't map IMAPMessage", e);
            return null;
        }
    }

    private String getSubject(IMAPMessage message) {
        try {
            return message.getSubject();
        } catch (MessagingException e) {
            log.error("Can't get a Subject");
            return "N/A";
        }
    }

    private String getAddress(IMAPMessage message) throws MessagingException {
        return Arrays.stream(message.getFrom())
            .map(a -> (InternetAddress) a)
            .map(InternetAddress::getAddress)
            .collect(Collectors.joining(", "));
    }

    private LocalDateTime getDate(IMAPMessage message) throws MessagingException {
        return message.getSentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
        MimeMultipart mimeMultipart
    ) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + html;
                //                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }
}
