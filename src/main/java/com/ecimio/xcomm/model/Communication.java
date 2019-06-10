package com.ecimio.xcomm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Communication {

    @Id
    private final String id;
    private final String message;
    private final String emailTo;
    private final String slackTo;
    private final String scheduledTime;

    @JsonCreator
    public Communication(@JsonProperty(value = "id") final String id,
                         @JsonProperty(value = "message", required = true) final String message,
                         @JsonProperty(value = "emailTo") final String emailTo,
                         @JsonProperty(value = "slackTo") final String slackTo,
                         @JsonProperty(value = "scheduledTime", required = true) final String scheduledTime
    ) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.message = message;
        this.emailTo = emailTo;
        this.slackTo = slackTo;
        this.scheduledTime = scheduledTime;
    }

    public Optional<String> getSlackTo() {
        return Optional.ofNullable(slackTo);
    }

    public List<CommunicationType> getTypes() {
        if (!this.emailTo.isEmpty() && !this.slackTo.isEmpty()) {
            return List.of(CommunicationType.EMAIL, CommunicationType.SLACK);
        } else if (!this.emailTo.isEmpty()) {
            return List.of(CommunicationType.EMAIL);
        } else if (!this.slackTo.isEmpty()) {
            return List.of(CommunicationType.SLACK);
        }
        return List.of();

    }

    public Optional<String> getEmailTo() {
        return Optional.ofNullable(emailTo);
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public enum CommunicationType {
        SLACK, EMAIL
    }

}
