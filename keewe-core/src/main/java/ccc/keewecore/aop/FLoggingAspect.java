package ccc.keewecore.aop;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class FLoggingAspect {

    private ThreadLocal<Map<String, Object>> mapThreadLocal = new ThreadLocal<>();
    private final static String KEY_START = "f_start";
    private final static String KEY_UUID = "f_uuid";

    @Before("@annotation(ccc.keewecore.aop.annotations.FLogging)")
    public void beforeLogging(JoinPoint joinPoint) {
        if (!log.isDebugEnabled()) return;
        Instant startTm = Instant.now();
        String uuid = UUID.randomUUID().toString();
        mapThreadLocal.set(Map.of(KEY_START, startTm, KEY_UUID, uuid));
        log.debug("{} :: [{}] Function start {}", uuid, joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "@annotation(ccc.keewecore.aop.annotations.FLogging)", returning = "returnValue")
    public void afterLogging(JoinPoint joinPoint, Object returnValue) {
        if (!log.isDebugEnabled()) return;
        Instant endTm = Instant.now();
        Map<String, Object> map = mapThreadLocal.get();
        if (map != null) {
            mapThreadLocal.remove();
            log.debug("{} :: [{}] Function end, Elapsed time = {}ms, Return = {}"
                    , map.get(KEY_UUID)
                    , joinPoint.getSignature().getName()
                    , Duration.between((Instant) map.get(KEY_START), endTm).toMillis(), returnValue);
        }
    }

    @AfterThrowing(pointcut = "@annotation(ccc.keewecore.aop.annotations.FLogging)", throwing = "cause")
    public void exceptionLogging(JoinPoint joinPoint, Throwable cause) {
        if (!log.isDebugEnabled()) return;
        Instant endTm = Instant.now();
        Map<String, Object> map = mapThreadLocal.get();
        if (map != null) {
            mapThreadLocal.remove();
            log.error("{} :: [{}] Function fail, Elapsed time = {}ms"
                    , map.get(KEY_UUID)
                    , joinPoint.getSignature().getName()
                    , Duration.between((Instant) map.get(KEY_START), endTm).toMillis(), cause);
        }
    }
}
