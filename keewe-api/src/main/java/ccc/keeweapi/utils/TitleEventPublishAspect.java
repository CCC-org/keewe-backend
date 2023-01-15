package ccc.keeweapi.utils;


import ccc.keeweapi.utils.annotations.TitleEventPublish;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class TitleEventPublishAspect {
    private final MQPublishService mqPublishService;

    @AfterReturning("@annotation(ccc.keeweapi.utils.annotations.TitleEventPublish)")
    public void publishTitleEvent(JoinPoint joinPoint) {
        Message message = MessageBuilder.withBody(new byte[0]).build();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        TitleEventPublish titleEventPublish = method.getAnnotation(TitleEventPublish.class);

        KeeweTitleHeader header = KeeweTitleHeader.of(titleEventPublish.titleCategory(), String.valueOf(SecurityUtil.getUserId()));
        log.info("[TEPA::publish] Title event publish. category={}, userId={}", header.getCategory(), header.getUserId());
        mqPublishService.publish(KeeweConsts.DEFAULT_ROUTING_KEY, KeeweConsts.TITLE_STAT_QUEUE,  message, header::toMessageWithHeader);
    }
}
