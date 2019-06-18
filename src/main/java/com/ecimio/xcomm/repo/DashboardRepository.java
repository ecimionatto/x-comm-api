package com.ecimio.xcomm.repo;

import com.ecimio.xcomm.model.Dashboard;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Transactional
public interface DashboardRepository extends ReactiveCrudRepository<Dashboard, String> {

    default Flux<Dashboard> incrementSent() {
        return this.findAll().take(1)
                .map(dashboard -> dashboard.incrementSent())
                .flatMap(this::save);

    }

    default Flux<Dashboard> incrementError() {
        return this.findAll().take(1)
                .map(dashboard -> dashboard.incrementError())
                .flatMap(this::save);
    }

}
