package ccc.keewepush.config;


import ccc.keewepush.dto.TitleEvent;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class PushAppConfig {

    @Bean
    Map<Long, Sinks.Many<TitleEvent>> userConnectionMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    Queue titleEventQueue(@Value("${env.node.name}") String nodeName) {
        return QueueBuilder.durable(nodeName).build();
    }

}
