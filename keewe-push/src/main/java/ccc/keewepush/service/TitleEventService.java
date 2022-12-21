package ccc.keewepush.service;


import ccc.keewepush.dto.TitleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class TitleEventService {

    private final Map<Long, Sinks.Many<TitleEvent>> userConnectionMap =
            new ConcurrentHashMap<>();


    public Flux<TitleEvent> createEventConn(Long userId) {
        Sinks.Many<TitleEvent> many = Sinks.many().unicast()
                                                  .onBackpressureBuffer();

        userConnectionMap.put(userId, many);
        many.tryEmitNext(TitleEvent.of("HAND-SHAKE", LocalDateTime.now()));
        return many.asFlux();
    }
}
