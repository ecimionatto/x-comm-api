package com.ecimio.xcomm.service;

import com.ecimio.xcomm.model.Communication;
import com.ecimio.xcomm.model.Settings;
import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackCommand implements MessageCommand {

    public void send(final Communication communication, final Settings settings) {

        Payload payload = Payload.builder()
                .channel(communication.getSlackTo().orElseThrow(IllegalStateException::new))
                .username("xcomm")
                .iconEmoji(":smile_cat:")
                .text(communication.getMessage())
                .build();

        Slack slack = Slack.getInstance();
        try {
            slack.send(settings.getSlackWebHook(), payload);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
