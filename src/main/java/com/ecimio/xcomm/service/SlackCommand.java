package com.ecimio.xcomm.service;


import com.github.seratch.jslack.*;
import com.github.seratch.jslack.api.webhook.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackCommand {

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
            slack.send("https://hooks.slack.com/services/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", payload);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
