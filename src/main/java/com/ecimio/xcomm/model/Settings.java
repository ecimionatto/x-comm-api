package com.ecimio.xcomm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Settings {

    @Id
    private final String id;
    private final String slackWebHook;
    private final String smtpUser;
    private final String smtpPass;

    public Settings(@JsonProperty(value = "id") final String id,
                    @JsonProperty(value = "slackWebHook") final String slackWebHook,
                    @JsonProperty(value = "smtpUser") final String smtpUser,
                    @JsonProperty(value = "smtpPass") final String smtpPass) {
        this.id = id;
        this.slackWebHook = slackWebHook;
        this.smtpUser = smtpUser;
        this.smtpPass = smtpPass;
    }

    public String getId() {
        return id;
    }

    public String getSlackWebHook() {
        return slackWebHook;
    }

    public String getSmtpUser() {
        return smtpUser;
    }

    public String getSmtpPass() {
        return smtpPass;
    }
}
