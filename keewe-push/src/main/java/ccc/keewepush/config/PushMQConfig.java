package ccc.keewepush.config;

import ccc.keewecore.consts.KeeweConsts;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;

public class PushMQConfig {
    @Bean
    Queue pushSendQueue() {
        return QueueBuilder.durable(KeeweConsts.PUSH_SEND_QUEUE).build();
    }

    @Bean
    Exchange pushSendExchange() {
        return ExchangeBuilder.directExchange(KeeweConsts.PUSH_SEND_EXCHANGE).build();
    }

    @Bean
    Binding pushSendExchange(Queue pushSendQueue, Exchange pushSendExchange) {
        return BindingBuilder.bind(pushSendQueue).to(pushSendExchange).with(KeeweConsts.DEFAULT_ROUTING_KEY).noargs();
    }
}
