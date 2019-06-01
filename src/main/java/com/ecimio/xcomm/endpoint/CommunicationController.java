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

@RestController
@RequestMapping(value = "/xcomm", consumes = APPLICATION_JSON_VALUE)
public class CommunicationController {

    @Autowired
    private CommunicationRepository communicationRepository;

    @GetMapping
    public Flux<Communication> getAll() {
        return communicationRepository.findAll();
    }

    @PostMapping
    public Mono<Communication> create(@Valid @RequestBody Communication communication) {
        return communicationRepository.save(communication);
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Communication>> getById(@PathVariable(value = "id") String tweetId) {
        return communicationRepository.findById(tweetId)
                .map(savedTweet -> ResponseEntity.ok(savedTweet))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public Mono<Communication> createOrUpdate(@PathVariable(value = "id") String id,
                                           @RequestBody Communication communication) {
        return communicationRepository.save(new Communication(id, communication.getMessage()));
    }

}
