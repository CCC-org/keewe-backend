package ccc.keewepush.listener;


import ccc.keewecore.dto.TitleEvent;
import ccc.keewepush.service.TitleEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TitleEventListener {
    private final TitleEventService titleEventService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${env.node.name}")
    public void onMessage(Message message) {
        try {
            byte[] body = message.getBody();
            TitleEvent titleEvent = objectMapper.readValue(body, TitleEvent.class);
            titleEventService.publishTitleEvent(titleEvent, getUser(message));
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
    }

    private Long getUser(Message message) {
        return (Long) message.getMessageProperties().getHeaders().get("userId");
    }
}