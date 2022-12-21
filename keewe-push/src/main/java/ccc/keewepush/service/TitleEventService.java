package ccc.keewepush.service;


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

    public Flux<TitleEvent> createEventConn(Long userId) {
        Sinks.Many<TitleEvent> eventSinks = userConnectionMap.putIfAbsent(userId, Sinks.many()
                .unicast()
                .onBackpressureBuffer());

        // missed event late push
        eventSinks.tryEmitNext(TitleEvent.of("HAND-SHAKE", LocalDateTime.now()));
        return eventSinks.asFlux()
                .doOnCancel(() -> userConnectionMap.remove(userId));
    }


    public void publishTitleEvent(Long userId) {
        Sinks.Many<TitleEvent> eventSinks = userConnectionMap.get(userId);

        if(eventSinks == null) {
            log.error("[TES::pushTitleEvent] Connection Loss. userId={}", userId);
        } else if(eventSinks.currentSubscriberCount() <= 0) {
            // save event for late push
        } else {
            TitleEvent sample = TitleEvent.of("sample", LocalDateTime.now());
            eventSinks.tryEmitNext(sample);
        }



    }
}
