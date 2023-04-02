package ccc.keeweapi.config.security.jwt;

import ccc.keeweapi.exception.KeeweAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.json.simple.JSONObject;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {

        commence(response, (KeeweAuthException) ex);
    }

    private void commence(HttpServletResponse response, KeeweAuthException kex) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JSONObject body = new JSONObject();
        body.put("code", kex.getKeeweRtnConsts().getCode());
        body.put("message", kex.getKeeweRtnConsts().getDescription());
        response.getWriter().print(body);
    }

}
