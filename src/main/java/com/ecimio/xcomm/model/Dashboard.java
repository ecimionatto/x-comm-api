package com.ecimio.xcomm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dashboard {

    @Id
    private final String id;
    private int sent;
    private int failed;

    public Dashboard(final String id, final int sent, final int failed) {
        this.id = id;
        this.sent = sent;
        this.failed = failed;
    }

    public String getId() {
        return id;
    }

    public int getSent() {
        return sent;
    }

    public int getFailed() {
        return failed;
    }

    public synchronized Dashboard incrementSent() {
        this.sent++;
        return this;
    }

    public synchronized Dashboard incrementError() {
        this.failed++;
        return this;
    }


}
