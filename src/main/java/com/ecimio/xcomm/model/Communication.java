package com.ecimio.xcomm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Communication {

    @Id
    private final String id;
    private final String message;
    private final String address;
    private final CommunicationType type;

    @JsonCreator
    public Communication(@JsonProperty("id") final String id,
                         @JsonProperty("message") final String message,
                         @JsonProperty("address") final String address,
                         @JsonProperty("type") final CommunicationType type) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.message = message;
        this.address = address;
        this.type = type;
    }

    public CommunicationType getType() {
        return type;
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

    public enum CommunicationType {
        SLACK, EMAIL
    }

}
