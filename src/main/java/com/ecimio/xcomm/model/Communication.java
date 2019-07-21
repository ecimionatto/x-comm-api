package com.ecimio.xcomm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
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
    private final String user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private final Date scheduledTime;
    private final CommunicationStatus status;
    private final String error;

    @JsonCreator
    public Communication(@JsonProperty(value = "id") final String id,
                         @JsonProperty(value = "message", required = true) final String message,
                         @JsonProperty(value = "emailTo") final String emailTo,
                         @JsonProperty(value = "slackTo") final String slackTo,
                         @JsonProperty(value = "user", required = true) final String user,
                         @JsonProperty(value = "scheduledTime", required = true) final Date scheduledTime,
                         @JsonProperty(value = "status") final CommunicationStatus status,
                         @JsonProperty(value = "error") final String error

    ) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.message = message;
        this.emailTo = emailTo;
        this.slackTo = slackTo;
        this.user = user;
        this.scheduledTime = scheduledTime;
        this.status = status == null ? CommunicationStatus.PENDING : status;
        this.error = error;
    }

    public String getUser() {
        return user;
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

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public Communication withStatus(CommunicationStatus communicationStatus) {
        return new Communication(this.id, this.message, this.emailTo,
                this.slackTo, this.user, this.scheduledTime, communicationStatus, null);
    }

    public Communication withUser(final String user) {
        return new Communication(this.id, this.message, this.emailTo,
                this.slackTo, user, this.scheduledTime, this.status, null);
    }

    public Communication withError(String error) {
        return new Communication(this.id, this.message, this.emailTo,
                this.slackTo, this.user, this.scheduledTime, CommunicationStatus.ERROR, error);
    }

    public CommunicationStatus getStatus() {
        return status;
    }

    public Optional<String> getError() {
        return Optional.ofNullable(error);
    }

    public boolean isPending() {
        return this.status == CommunicationStatus.PENDING;
    }

    public enum CommunicationType {
        SLACK, EMAIL
    }

    public enum CommunicationStatus {
        PENDING, SENT, ERROR, DELETED
    }

}
