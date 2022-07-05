package ccc.keeweapi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import org.json.simple.JSONObject;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {

        String exception = String.valueOf(request.getAttribute("exception"));
        log.info("ex {}", exception);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject body = new JSONObject();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", 400);
        body.put("message", ex.getMessage());
        response.getWriter().print(body);

//
//        if("TOKEN_NOT_VALID".equals(exception))
//            sendResponse(response, ex.getMessage());
//        else if("TOKEN_EXPIRED".equals(exception))
//            sendResponse(response,  ex.getMessage());
//        else
//            sendResponse(response, ex.getMessage());
//
//        log.error("토큰 필터에서 발생: {}", ex.getMessage());

    }

    private void sendResponse(HttpServletResponse response, String message) throws IOException {


    }

}
