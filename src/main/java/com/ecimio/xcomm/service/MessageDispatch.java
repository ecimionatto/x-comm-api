package com.ecimio.xcomm.service;

import com.ecimio.xcomm.model.Communication;
import com.ecimio.xcomm.repo.CommunicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageDispatch {

    private final EmailCommand emailCommand;
    private final SlackCommand slackCommand;
    private final CommunicationRepository communicationRepository;

    @Autowired
    public MessageDispatch(final EmailCommand emailCommand, final SlackCommand slackCommand, final CommunicationRepository communicationRepository) {
        this.emailCommand = emailCommand;
        this.slackCommand = slackCommand;
        this.communicationRepository = communicationRepository;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void send() {
        communicationRepository.findAll().map( communication -> {
            if (communication.getTypes() != null ) {
                if (communication.getTypes().contains(Communication.CommunicationType.EMAIL)) {
                    emailCommand.send(communication);
                }
                if (communication.getTypes().contains(Communication.CommunicationType.SLACK)) {
                    slackCommand.send(communication);
                }
            }
            communicationRepository.delete(communication);
            return communication;
        }).subscribe();
    }

}
