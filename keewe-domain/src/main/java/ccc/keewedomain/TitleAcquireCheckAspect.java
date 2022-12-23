package ccc.keewedomain;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keeweinfra.service.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Aspect
public class TitleAcquireCheckAspect {
    private final MQPublishService mqPublishService;

    @AfterReturning(value = "@annotation(ccc.keewedomain.TitleAcquireCheck)", returning = "result")
    void publishEventIfStandardMeet(Boolean result) {
        if(result) {
            // how to know title category
            mqPublishService.publish(KeeweConsts.TITLE_ACQUIREMENT_EXCHANGE, null);

        }
    }
}
