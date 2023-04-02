package ccc.keeweapi.config.interceptor;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
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
        try {
            MDC.put("user_id", SecurityUtil.getUserId().toString());
            MDC.put("trace_id", UUID.randomUUID().toString());
        } catch (KeeweException ex) {
            if(!KeeweRtnConsts.ERR404.equals(ex.getKeeweRtnConsts())) {
                throw ex;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.clear();
    }
}
