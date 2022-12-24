package ccc.keeweinfra.service;

import ccc.keewecore.consts.KeeweConsts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MQPublishService {
    private final RabbitTemplate rabbitTemplate;

    //TODO: retry handler 도입
    public <T> void publish(String exchange, T body) {
        rabbitTemplate.convertAndSend(exchange
                , KeeweConsts.DEFAULT_ROUTING_KEY
                , body);
    }

    public <T> void publish(String queueName, Message message) {
        rabbitTemplate.convertAndSend(
                KeeweConsts.DEFAULT_ROUTING_KEY
                , queueName
                , message);
    }
}
