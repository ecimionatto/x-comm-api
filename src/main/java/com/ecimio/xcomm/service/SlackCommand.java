package com.ecimio.xcomm.service;

import com.ecimio.xcomm.model.Communication;
import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackCommand implements MessageCommand {

    @Value("${slack.webhook}")
    private String webhook;

    public void send(final Communication communication) {

        Payload payload = Payload.builder()
                .channel(communication.getSlackTo().orElseThrow(IllegalStateException::new))
                .username("xcomm")
                .iconEmoji(":smile_cat:")
                .text(communication.getMessage())
                .build();

        Slack slack = Slack.getInstance();
        try {
            slack.send(webhook, payload);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
