package com.ecimio.xcomm.repo;

import com.ecimio.xcomm.model.Communication;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CommunicationRepository extends ReactiveCrudRepository<Communication, String> {

}
