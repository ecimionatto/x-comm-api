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
public class Template {

    @Id
    private final String id;
    private final String message;

    @JsonCreator
    public Template(@JsonProperty(value = "id") final String id,
                    @JsonProperty(value = "message", required = true) final String message

    ) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }


}
