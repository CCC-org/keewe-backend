package ccc.keeweapi.controller.ui;

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
@RequestMapping("/api/v1/oauth/google")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GoogleAuthController {

    @Value("${google.account-url}")
    private String googleUrl;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.redirect-url}")
    private String redirectUrl;

    @Value("${google.scope}")
    private String scope;

    private final String responseType = "code";

    private String authRequestUrl;

    @PostConstruct
    private void initAuthRequestUrl() {
        UriComponents uri = UriComponentsBuilder.fromUriString(googleUrl)
                .pathSegment("o", "oauth2", "v2", "auth")
                .queryParam("response_type", responseType)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUrl)
                .queryParam("scope", scope)
                .build();

        authRequestUrl = uri.toString();
    }

    @GetMapping
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect(authRequestUrl);
    }
}
