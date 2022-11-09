package ccc.keewecore.aop;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ApiBlockingAspect {

    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @Around("@annotation(ccc.keewecore.aop.annotations.LocalOnlyApi)")
    public <T> T checkApiAcceptingCondition(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[ABA::checkApiAcceptingCondition] environment={}", profile);
        if("local".equals(profile)) {
            return (T) joinPoint.proceed();
        }

        throw new KeeweException(KeeweRtnConsts.ERR999);
    }
}
