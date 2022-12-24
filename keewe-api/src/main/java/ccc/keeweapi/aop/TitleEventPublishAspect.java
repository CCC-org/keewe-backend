package ccc.keeweapi.aop;


import ccc.keeweapi.aop.annotations.TitleEventPublish;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keeweinfra.service.MQPublishService;
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

    @AfterReturning("@annotation(ccc.keeweapi.aop.annotations.TitleEventPublish)")
    public void publishTitleEvent(JoinPoint joinPoint) {
        Message message = MessageBuilder.withBody(new byte[0]).build();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        TitleEventPublish titleEventPublish = method.getAnnotation(TitleEventPublish.class);

        KeeweTitleHeader header = KeeweTitleHeader.of(titleEventPublish.titleCategory(), String.valueOf(SecurityUtil.getUserId()));
        mqPublishService.publish(KeeweConsts.TITLE_STAT_QUEUE, header.toMessageWithHeader(message));

    }
}