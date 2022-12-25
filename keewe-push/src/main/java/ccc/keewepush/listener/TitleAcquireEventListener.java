package ccc.keewepush.listener;


import ccc.keewepush.service.TitleEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TitleAcquireEventListener {
    private final TitleEventService titleEventService;

    @RabbitListener(queues = "${env.node.name}")
    public void onMessage(Message message) {
        try {
            titleEventService.publishTitleAcquireEvent(message);
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
    }
}