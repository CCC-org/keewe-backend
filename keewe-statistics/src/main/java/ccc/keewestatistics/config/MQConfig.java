package ccc.keewestatistics.config;

import ccc.keewecore.consts.KeeweConsts;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Bean
    Queue insightViewQueue() {
        return QueueBuilder.durable(KeeweConsts.INSIGHT_VIEW_QUEUE).build();
    }

    @Bean
    Exchange insightViewExchange() {
        return ExchangeBuilder.directExchange(KeeweConsts.INSIGHT_VIEW_EXCHANGE).build();
    }

    @Bean
    Binding insightViewBinding(Queue insightViewQueue, Exchange insightViewExchange) {
        return BindingBuilder.bind(insightViewQueue).to(insightViewExchange).with(KeeweConsts.DEFAULT_ROUTING_KEY).noargs();
    }

}
