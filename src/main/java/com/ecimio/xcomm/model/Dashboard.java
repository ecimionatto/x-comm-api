package com.ecimio.xcomm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dashboard {

    @Id
    private final String id = UUID.randomUUID().toString();

    private final AtomicInteger sent;
    private final AtomicInteger error;

    public Dashboard() {
        this.sent = new AtomicInteger();
        this.error = new AtomicInteger();
    }

    public AtomicInteger getSent() {
        return sent;
    }

    public AtomicInteger getError() {
        return error;
    }

    public Dashboard incrementSent() {
        sent.incrementAndGet();
        return this;
    }

    public Dashboard incrementError() {
        error.incrementAndGet();
        return this;
    }


}
