package ccc.keewepush.service;


import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.dto.TitleEvent;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewecore.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
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
        eventSinks.tryEmitNext(TitleEvent.of(null, KeeweConsts.EVENT_CONNECTION_HANDSHAKE, KeeweConsts.EVENT_CONNECTION_HANDSHAKE, String.valueOf(LocalDateTime.now())));

        return eventSinks.asFlux()
                        .doOnError(ex -> failover(ex, userId))
                        .doFinally(event -> {
                            log.info("[TES::createEventAsFlux] Connection closed. Sink drop.");
                            userConnectionMap.remove(userId);
                        });
    }

    public void publishTitleAcquireEvent(Message message) {
        KeeweTitleHeader header = KeeweTitleHeader.toHeader(message);
        log.info("[TES::publishTitleAcquireEvent] category={} userId={} body={}", header.getCategory(), header.getUserId(), new String(message.getBody()));
        Sinks.Many<TitleEvent> eventSinks = userConnectionMap.get(Long.valueOf(header.getUserId()));

        if(eventSinks == null) {
            log.warn("[TES::publishTitleAcquireEvent] Connection not found. userId={}", header.getUserId());
            return;
        }

        TitleEvent titleEvent = ObjectMapperUtils.readValue(message.getBody(), TitleEvent.class);
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
