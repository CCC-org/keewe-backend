package ccc.keeweinfra.service.messagequeue;

import ccc.keewecore.consts.KeeweConsts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MQPublishService {
    private final RabbitTemplate rabbitTemplate;

    //TODO: retry handler 도입
    public <T> void publish(String exchange, T body) {
        rabbitTemplate.convertAndSend(exchange, KeeweConsts.DEFAULT_ROUTING_KEY, body);
    }

    public void publish(String exchange, String queueName, Message message, MessagePostProcessor messagePostProcessor) {
        rabbitTemplate.convertAndSend(exchange, queueName, message, messagePostProcessor);
    }

}
