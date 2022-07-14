package ccc.keeweapi.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/api/v1/oauth/kakao")
@CrossOrigin(origins = "*", maxAge = 3600)
public class KakaoAuthController {
    @Value("${kakao.auth-url}")
    private String kakaoUrl;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    @GetMapping
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoUrl + "/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUrl + "&response_type=code");
    }
}
