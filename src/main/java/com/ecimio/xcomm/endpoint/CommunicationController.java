package com.ecimio.xcomm.endpoint;

import com.ecimio.xcomm.model.Communication;
import com.ecimio.xcomm.repo.CommunicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@RequestMapping(value = "/xcomm", consumes = APPLICATION_JSON_VALUE)
public class CommunicationController {

    private CommunicationRepository communicationRepository;

    public CommunicationController(@Autowired final CommunicationRepository communicationRepository) {
        this.communicationRepository = communicationRepository;
    }

    @GetMapping
    public Flux<Communication> getAll() {
        return communicationRepository
                .findAll()
                .filter(Communication::isPending);
    }

    @PostMapping
    public Flux<Communication> create(@Valid @RequestBody Communication communication) {
        return communicationRepository.saveWithRecurrence(communication);
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Communication>> getById(@PathVariable(value = "id") String id) {
        return communicationRepository.findById(id)
                .map(savedCommunication -> ResponseEntity.ok(savedCommunication))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public Mono<Communication> createOrUpdate(@PathVariable(value = "id") String id,
                                              @RequestBody Communication communication) {
        return communicationRepository.save(new Communication(id, communication.getMessage(),
                communication.getEmailTo().orElse(null),
                communication.getSlackTo().orElse(null),
                communication.getUser(),
                communication.getScheduledTime(),
                null,
                null,
                communication.getRecurrence()));
    }

    @DeleteMapping("{id}")
    public Mono<Communication> delete(@PathVariable(value = "id") final String id,
                                      @RequestBody final String user) {
        return communicationRepository.findById(id)
                .flatMap(communication -> communicationRepository.save(communication
                        .withStatus(Communication.CommunicationStatus.DELETED)
                        .withUser(user)));
    }


}
