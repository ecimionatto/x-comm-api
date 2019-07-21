package com.ecimio.xcomm.repo;

import com.ecimio.xcomm.model.Communication;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Transactional
public interface CommunicationRepository extends ReactiveCrudRepository<Communication, String> {

    default Flux<Communication> saveWithRecurrence(final Communication communication) {
        return Flux.just(communication).flatMap(comm -> {
            final LocalDateTime localDateTime = LocalDateTime.ofInstant(comm.getScheduledTime().toInstant(), ZoneId.systemDefault());
            if (comm.getRecurrence() == Communication.CommunicationRecurrence.DAILY) {
                return Flux.just(comm,
                        comm.newInstanceWithScheduledTime(localDateTime.plusDays(1)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusDays(2)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusDays(3)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusDays(4)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusDays(4)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusDays(5)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusDays(6)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusDays(7)));
            } else if (comm.getRecurrence() == Communication.CommunicationRecurrence.WEEKLY) {
                return Flux.just(comm,
                        comm.newInstanceWithScheduledTime(localDateTime.plusWeeks(1)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusWeeks(2)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusWeeks(3)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusWeeks(4)));
            } else if (comm.getRecurrence() == Communication.CommunicationRecurrence.MONTHLY) {
                return Flux.just(comm,
                        comm.newInstanceWithScheduledTime(localDateTime.plusMonths(1)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusMonths(2)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusMonths(3)),
                        comm.newInstanceWithScheduledTime(localDateTime.plusMonths(4)));
            } else {
                return Flux.just(comm);
            }
        }).flatMap(this::save);
    }

}
