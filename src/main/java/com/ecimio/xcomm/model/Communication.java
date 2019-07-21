package com.ecimio.xcomm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
    private final CommunicationRecurrence recurrence;

    @JsonCreator
    public Communication(@JsonProperty(value = "id") final String id,
                         @JsonProperty(value = "message", required = true) final String message,
                         @JsonProperty(value = "emailTo") final String emailTo,
                         @JsonProperty(value = "slackTo") final String slackTo,
                         @JsonProperty(value = "user", required = true) final String user,
                         @JsonProperty(value = "scheduledTime", required = true) final Date scheduledTime,
                         @JsonProperty(value = "status") final CommunicationStatus status,
                         @JsonProperty(value = "error") final String error,
                         @JsonProperty(value = "recurrence") final CommunicationRecurrence recurrence) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.message = message;
        this.emailTo = emailTo;
        this.slackTo = slackTo;
        this.user = user;
        this.scheduledTime = scheduledTime;
        this.status = status == null ? CommunicationStatus.PENDING : status;
        this.error = error;
        this.recurrence = recurrence;
    }

    public CommunicationRecurrence getRecurrence() {
        return recurrence;
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
                this.slackTo, this.user, this.scheduledTime, communicationStatus, null, this.recurrence);
    }

    public Communication withUser(final String user) {
        return new Communication(this.id, this.message, this.emailTo,
                this.slackTo, user, this.scheduledTime, this.status, null, this.recurrence);
    }

    public Communication withError(String error) {
        return new Communication(this.id, this.message, this.emailTo,
                this.slackTo, this.user, this.scheduledTime, CommunicationStatus.ERROR, error, this.recurrence);
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

    public enum CommunicationRecurrence {
        NEVER, DAILY, WEEKLY, MONTHLY
    }

    public Communication newInstanceWithScheduledTime(final LocalDateTime time) {
        final Instant instant = time.toInstant(OffsetDateTime.now().getOffset());
        return new Communication(null, this.message, this.emailTo,
                this.slackTo,
                this.user,
                Date.from(instant),
                this.status,
                null,
                this.recurrence);
    }

}
