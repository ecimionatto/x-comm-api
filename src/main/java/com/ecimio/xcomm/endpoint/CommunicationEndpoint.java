package com.ecimio.xcomm.endpoint;

import com.ecimio.xcomm.model.Communication;
import com.ecimio.xcomm.repo.CommunicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class CommunicationEndpoint {

    private final CommunicationRepository communicationRepository;

    public CommunicationEndpoint(@Autowired final CommunicationRepository communicationRepository) {
        this.communicationRepository = communicationRepository;
    }

    public Mono<ServerResponse> put(ServerRequest request) {
        final Mono<Communication> communication = request.bodyToMono(Communication.class);
        final String id = request.pathVariable("id");

        final Mono<Communication> savedCom = communication.flatMap(requestCom ->
                communicationRepository.findById(id)
                        .flatMap(existing -> communicationRepository.save(new Communication(existing.getId(), requestCom.getMessage())))
                        .switchIfEmpty(communicationRepository.save(new Communication(id, requestCom.getMessage())))
        );

        final BodyInserter<Mono<Communication>, ReactiveHttpOutputMessage> inserter =
                fromPublisher(savedCom, Communication.class);

        return ok().contentType(APPLICATION_JSON)
                .body(inserter);
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        final BodyInserter<Mono<Communication>, ReactiveHttpOutputMessage> inserter =
                fromPublisher(communicationRepository.findById(request.pathVariable("id")), Communication.class);
        return ok().contentType(APPLICATION_JSON)
                .body(inserter);
    }


    /*

    public Mono<ServerResponse> get(ServerRequest request) {
    final UUID id = UUID.fromString(request.pathVariable("id"));
    final Mono<Person> person = personManager.findById(id);
    return person
        .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromPublisher(person, Person.class)))
        .switchIfEmpty(notFound().build());
  }
  public Mono<ServerResponse> all(ServerRequest request) {
    return ok().contentType(APPLICATION_JSON)
        .body(fromPublisher(personManager.findAll(), Person.class));
  }
  public Mono<ServerResponse> put(ServerRequest request) {
    final UUID id = UUID.fromString(request.pathVariable("id"));
    final Mono<Person> person = request.bodyToMono(Person.class);
    return personManager
        .findById(id)
        .flatMap(
            old ->
                ok().contentType(APPLICATION_JSON)
                    .body(
                        fromPublisher(
                            person
                                .map(p -> new Person(p, id))
                                .flatMap(p -> personManager.update(old, p)),
                            Person.class)))
        .switchIfEmpty(notFound().build());
  }
  public Mono<ServerResponse> post(ServerRequest request) {
    final Mono<Person> person = request.bodyToMono(Person.class);
    final UUID id = UUID.randomUUID();
    return created(UriComponentsBuilder.fromPath("people/" + id).build().toUri())
        .contentType(APPLICATION_JSON)
        .body(
            fromPublisher(
                person.map(p -> new Person(p, id)).flatMap(personManager::save), Person.class));
  }
  public Mono<ServerResponse> delete(ServerRequest request) {
    final UUID id = UUID.fromString(request.pathVariable("id"));
    return personManager
        .findById(id)
        .flatMap(p -> noContent().build(personManager.delete(p)))
        .switchIfEmpty(notFound().build());
  }
  public Mono<ServerResponse> getByCountry(ServerRequest serverRequest) {
    final String country = serverRequest.pathVariable("country");
    return ok().contentType(APPLICATION_JSON)
        .body(fromPublisher(personManager.findAllByCountry(country), Person.class));
  }

     */

}
