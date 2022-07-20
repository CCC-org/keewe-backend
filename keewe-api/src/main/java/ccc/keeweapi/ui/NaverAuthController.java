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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Controller
@RequestMapping("/api/v1/oauth/naver")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NaverAuthController {

    private final SecureRandom secureRandom;

    @Value("${naver.auth-url}")
    private String naverUrl;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.redirect-url}")
    private String redirectUrl;

    @Value("${naver.state-name}")
    private String naverState;

    private String responseType = "code";

    private String authRequestUrl;

    @PostConstruct
    private void initAuthRequestUrl() {
        UriComponents uri = UriComponentsBuilder.fromUriString(naverUrl)
                .pathSegment("oauth2.0", "authorize")
                .queryParam("response_type", responseType)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUrl)
                .build();

        authRequestUrl = uri.toString();
    }

    public NaverAuthController() throws NoSuchAlgorithmException {
        this.secureRandom = SecureRandom.getInstance("SHA1PRNG");
    }

    @GetMapping
    public void redirect(HttpSession session, HttpServletResponse response) throws IOException {
        String state = generateState();
        session.setAttribute(naverState, state);
        response.sendRedirect(authRequestUrl + "&state=" + state);
    }

    private String generateState() {
        int stateInt = Math.abs(secureRandom.nextInt());
        return Integer.toString(stateInt);
    }
}
