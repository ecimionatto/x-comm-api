package com.ecimio.xcomm.repo;

import com.ecimio.xcomm.model.Communication;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CommunicationRepository extends ReactiveCrudRepository<Communication, String> {

}
