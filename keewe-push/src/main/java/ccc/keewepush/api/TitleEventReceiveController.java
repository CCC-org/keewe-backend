package ccc.keewepush.api;


import ccc.keewecore.dto.TitleEvent;
import ccc.keewepush.service.TitleEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TitleEventReceiveController {
    private final TitleEventService titleEventService;

    //FIXME userId 시큐리티
    @GetMapping("/{userId}")
    public Flux<TitleEvent> createEventConnection(@PathVariable Long userId) {
        return titleEventService.createEventAsFlux(userId);
    }

}
