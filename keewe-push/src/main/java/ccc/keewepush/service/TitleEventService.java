package ccc.keewepush.service;


import ccc.keewecore.consts.KeeweConsts;
import ccc.keewepush.dto.TitleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TitleEventService {
    private final Map<Long, Sinks.Many<TitleEvent>> userConnectionMap;

    public Flux<TitleEvent> createEventAsFlux(Long userId) {
        makeAndPutConnectionIfAbsent(userId);

        Sinks.Many<TitleEvent> eventSinks = userConnectionMap.get(userId);
        log.info("[TES::createEventAsFlux] Consumers userId={}, Subs count={}", userId, eventSinks.currentSubscriberCount());

        //TODO missed event late push
        eventSinks.tryEmitNext(TitleEvent.of(KeeweConsts.EVENT_CONNECTION_HANDSHAKE, LocalDateTime.now()));

        return eventSinks.asFlux()
                        .doOnError(ex -> failover(ex, userId))
                        .doFinally(event -> {
                            log.info("[TES::createEventAsFlux] Connection closed. Sink drop.");
                            userConnectionMap.remove(userId);
                        });
    }

    public void publishTitleEvent(TitleEvent titleEvent, Long userId) {
        Sinks.Many<TitleEvent> eventSinks = userConnectionMap.get(userId);

        if(eventSinks == null) {
            log.warn("[TES::publishTitleEvent] Connection not found. userId={}", userId);
            return;
        }

        eventSinks.tryEmitNext(titleEvent);
    }

    private void makeAndPutConnectionIfAbsent(Long userId) {
        userConnectionMap.putIfAbsent(userId, Sinks.many()
                        .unicast()
                        .onBackpressureBuffer());
    }


    private void failover(Throwable t, Long userId) {
        Sinks.Many<TitleEvent> eventSinks = userConnectionMap.remove(userId);

        if (eventSinks != null) {
            eventSinks.tryEmitError(t);
        }
    }


}
