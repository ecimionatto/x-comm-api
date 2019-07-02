package com.ecimio.xcomm.service;

import com.ecimio.xcomm.model.Communication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailCommand {

    private static final Logger logger = LogManager.getLogger(EmailCommand.class);

    @Value("${email.user}")
    private String user;

    @Value("${email.pass}")
    private String pass;

    public void send(final Communication communication) {

        logger.debug("send started");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("xcomm.message@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(communication.getEmailTo().orElseThrow(IllegalStateException::new))
            );
            message.setSubject("XComm Communication");
            message.setText(communication.getMessage());

            Transport.send(message);

            logger.debug("send completed message={}", message);

        } catch (MessagingException e) {
            logger.error("send error", e);
            throw new IllegalStateException(e);
        }
    }
}
