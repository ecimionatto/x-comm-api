package com.ecimio.xcomm.service;

import com.ecimio.xcomm.model.Communication;

public interface MessageCommand {
    void send(Communication communication);
}
