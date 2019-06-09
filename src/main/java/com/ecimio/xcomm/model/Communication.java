package com.ecimio.xcomm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Communication {

    @Id
    private final String id;
    private final String message;
    private final String address;
    private final List<CommunicationType> types;
    private final String scheduledTime;

    @JsonCreator
    public Communication(@JsonProperty(value = "id", required = true) final String id,
                         @JsonProperty(value = "message", required = true) final String message,
                         @JsonProperty(value = "address", required = true) final String address,
                         @JsonProperty(value = "scheduledTime", required = true) final String scheduledTime,
                         @JsonProperty(value = "types", required = true) final List<CommunicationType> types
    ) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.message = message;
        this.address = address;
        this.types = types;
        this.scheduledTime = scheduledTime;
    }

    public List<CommunicationType> getTypes() {
        return types;
    }

    public String getAddress() {
        return address;
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
