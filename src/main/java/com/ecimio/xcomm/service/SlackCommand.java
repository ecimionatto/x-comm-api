package com.ecimio.xcomm.service;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackCommand {

    @Value("${slack.webhook}")
    private String webhook;

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void sendMessages() {

        Payload payload = Payload.builder()
                .channel("#x-comm-test")
                .username("jSlack Bot")
                .iconEmoji(":smile_cat:")
                .text("Hello World!")
                .build();

        Slack slack = Slack.getInstance();
        try {
            slack.send(webhook, payload);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
