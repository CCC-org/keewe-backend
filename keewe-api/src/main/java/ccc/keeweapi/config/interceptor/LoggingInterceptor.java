package ccc.keeweapi.config.interceptor;

import ccc.keeweapi.utils.SecurityUtil;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(SecurityUtil.getUser() != null) {
            MDC.put("user_id", SecurityUtil.getUserId().toString());
            // note. 불필요한 trace_id 생성 방지
            MDC.put("trace_id", UUID.randomUUID().toString());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.clear();
    }
}
