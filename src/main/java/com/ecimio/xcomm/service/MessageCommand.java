package com.ecimio.xcomm.service;

import com.ecimio.xcomm.model.Communication;
import com.ecimio.xcomm.model.Settings;

public interface MessageCommand {
    void send(Communication communication, Settings settings);
}
