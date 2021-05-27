package com.bsuir.service;

import com.bsuir.map.ImapMessageMapper;
import com.bsuir.repository.entity.EMail;
import com.bsuir.repository.entity.SendingMessage;
import com.bsuir.repository.entity.UserCredentials;
import com.sun.mail.imap.IMAPMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailClientServiceImpl implements MailClientService {

    private final ImapMessageMapper mapper;

    @Override
    public Collection<EMail> receiveEmails(UserCredentials userCredentials) {
        Collection<EMail> result = new ArrayList<>();
        try {
            String host = userCredentials.getHost();
            String username = userCredentials.getLogin();
            String password = userCredentials.getPassword();

            Properties props = new Properties();
            props.setProperty("mail.imap.ssl.enable", "true");
            props.put("mail.debug", "true");

            Session session = javax.mail.Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();

            for (Message currentMessage : messages) {
                result.add(mapper.mapToEmail((IMAPMessage) currentMessage));
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            log.error("Can't receive emails", e);
        }
        return result;
    }

    private Session getSession(UserCredentials credentials) {
        Properties props = new Properties();
        props.put("mail.smtp.host", credentials.getHost()); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.debug", "true");

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(credentials.getLogin(), credentials.getPassword());
            }
        };
        return Session.getInstance(props, auth);
    }

    @Override
    public void sendEmail(UserCredentials credentials, SendingMessage message) {
        try {
            MimeMessage msg = new MimeMessage(getSession(credentials));
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(credentials.getLogin()));
            msg.setReplyTo(InternetAddress.parse(credentials.getLogin(), false));

            // set message fields
            msg.setSubject(message.getSubject(), "UTF-8");
            msg.setText(message.getText(), "UTF-8");
            msg.setSentDate(Date.from(message.getDate().toInstant(ZoneOffset.UTC)));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.getTo(), false));

            Transport.send(msg);

            log.info("Message was sent successfully");
        } catch (Exception e) {
            log.error("Can't send a message", e);
        }
    }
}
