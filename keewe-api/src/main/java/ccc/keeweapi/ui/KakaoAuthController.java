package ccc.keeweapi.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
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

    private String responseType = "code";

    private String authRequestUrl;

    @PostConstruct
    private void initAuthRequestUrl() {
        UriComponents uri = UriComponentsBuilder.fromUriString(kakaoUrl)
                .pathSegment("oauth", "authorize")
                .queryParam("response_type", responseType)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUrl)
                .build();

        authRequestUrl = uri.toString();
    }

    @GetMapping
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect(authRequestUrl);
    }
}
