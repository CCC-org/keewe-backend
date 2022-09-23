package ccc.keewestatistics;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {


    @RabbitListener
    public void onMessage() {

    }
}
