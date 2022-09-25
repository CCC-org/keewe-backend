package ccc.keeweinfra.service;

import ccc.keewecore.consts.KeeweConsts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MQPublishService {
    private final RabbitTemplate rabbitTemplate;

    //TODO: retry handler 도입
    public void publish(Object body) {
        rabbitTemplate.convertAndSend(KeeweConsts.INSIGHT_VIEW_EXCHANGE
                , KeeweConsts.DEFAULT_ROUTING_KEY
                , body);
    }
}
