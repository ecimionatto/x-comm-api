package com.ecimio.xcomm.repo;

import com.ecimio.xcomm.model.Dashboard;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Transactional
public interface DashboardRepository extends ReactiveCrudRepository<Dashboard, String> {

    default Flux<Dashboard> incrementSent() {
        return this.findAll()
                .switchIfEmpty(this.save(new Dashboard(UUID.randomUUID().toString(), 0, 0)))
                .take(1)
                .doOnEach(System.out::println)
                .map(dashboard -> dashboard.incrementSent())
                .flatMap(this::save);

    }

    default Flux<Dashboard> incrementError() {
        return this.findAll()
                .switchIfEmpty(this.save(new Dashboard(UUID.randomUUID().toString(), 0, 0)))
                .take(1)
                .map(dashboard -> dashboard.incrementError())
                .flatMap(this::save);
    }

}
