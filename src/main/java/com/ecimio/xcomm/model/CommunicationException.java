package com.ecimio.xcomm.model;

public class CommunicationException extends RuntimeException {
    public CommunicationException(String communicationId, Exception e) {
        super(e);
    }
}
