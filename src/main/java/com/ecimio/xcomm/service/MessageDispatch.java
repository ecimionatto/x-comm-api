package com.ecimio.xcomm.service;

import com.ecimio.xcomm.model.Communication;
import com.ecimio.xcomm.model.CommunicationException;
import com.ecimio.xcomm.repo.CommunicationRepository;
import com.ecimio.xcomm.repo.DashboardRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(MessageDispatch.class);

    private final EmailCommand emailCommand;
    private final SlackCommand slackCommand;
    private final CommunicationRepository communicationRepository;
    private final ReactiveMongoOperations reactiveMongoOperations;
    private final DashboardRepository dashboardRepository;

    @Autowired
    public MessageDispatch(final EmailCommand emailCommand,
                           final SlackCommand slackCommand,
                           final CommunicationRepository communicationRepository,
                           final DashboardRepository dashboardRepository,
                           final ReactiveMongoOperations reactiveMongoOperations) {
        this.emailCommand = emailCommand;
        this.slackCommand = slackCommand;
        this.communicationRepository = communicationRepository;
        this.dashboardRepository = dashboardRepository;
        this.reactiveMongoOperations = reactiveMongoOperations;
    }

    @Transactional
    @Scheduled(fixedDelay = 1000 * 30 * 1)
    public void send() {
        logger.debug("send started");
        communicationRepository
                .findAll()
                .filter(communication -> communication.getStatus() == Communication.CommunicationStatus.PENDING
                        && communication.getScheduledTime().toInstant().isBefore(Instant.now()))
                .map(this::attemptToSend)
                .onErrorMap(CommunicationException.class, e -> handleError(e))
                .map(communication -> communication.withStatus(Communication.CommunicationStatus.SENT))
                .flatMap(reactiveMongoOperations::remove)
                .flatMap(comm -> dashboardRepository.incrementSent())
                .then().subscribe();
        logger.debug("send completed");
    }

    private Throwable handleError(final CommunicationException e) {
        logger.debug("handleError started");
        final Mono<Communication> byId = communicationRepository.findById(e.getMessage())
                .map(communication -> communication.withError(e.getMessage()));
        reactiveMongoOperations.save(byId).subscribe();
        dashboardRepository.incrementError().subscribe();

        logger.debug("handleError completed", e);
        return e;
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
