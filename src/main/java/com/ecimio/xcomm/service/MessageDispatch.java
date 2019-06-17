package com.ecimio.xcomm.service;

import com.ecimio.xcomm.model.Communication;
import com.ecimio.xcomm.model.CommunicationException;
import com.ecimio.xcomm.repo.CommunicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Transactional
@Service
public class MessageDispatch {

    private final EmailCommand emailCommand;
    private final SlackCommand slackCommand;
    private final CommunicationRepository communicationRepository;
    private ReactiveMongoOperations reactiveMongoOperations;

    @Autowired
    public MessageDispatch(final EmailCommand emailCommand, final SlackCommand slackCommand,
                           final CommunicationRepository communicationRepository, final ReactiveMongoOperations reactiveMongoOperations) {
        this.emailCommand = emailCommand;
        this.slackCommand = slackCommand;
        this.communicationRepository = communicationRepository;
        this.reactiveMongoOperations = reactiveMongoOperations;
    }

    @Transactional
    @Scheduled(fixedDelay = 1000 * 60 * 1)
    public void send() {
        communicationRepository.findAll()
                .filter(communication -> communication.getStatus() == Communication.CommunicationStatus.PENDING
                        && communication.getScheduledTime().toInstant().isBefore(Instant.now()))
                .map(this::attemptToSend)
                .onErrorMap(CommunicationException.class, e -> {
                    final Mono<Communication> byId = communicationRepository.findById(e.getMessage());
                    reactiveMongoOperations.save(byId);
                    return e;
                })
                .map(communication -> communication.withStatus(Communication.CommunicationStatus.SENT))
                .flatMap(reactiveMongoOperations::remove)
                .then().subscribe();
    }

    private Communication attemptToSend(final Communication communication) {
        try {
            if (communication.getTypes().contains(Communication.CommunicationType.EMAIL)) {
                emailCommand.send(communication);
            }
            if (communication.getTypes().contains(Communication.CommunicationType.SLACK)) {
                slackCommand.send(communication);
            }
        } catch (Exception e) {
            throw new CommunicationException(communication.getId(), e);
        }
        return communication;
    }

}
