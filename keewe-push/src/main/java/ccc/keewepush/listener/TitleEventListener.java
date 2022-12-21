package ccc.keewepush.listener;


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
            titleEventService.publishTitleEvent(1L);
        } catch (Throwable t) {

        }

    }
}
