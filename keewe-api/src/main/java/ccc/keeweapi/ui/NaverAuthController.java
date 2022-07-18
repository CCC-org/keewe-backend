package ccc.keeweapi.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/api/v1/oauth/naver")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NaverAuthController {
    @Value("${naver.auth-url}")
    private String naverUrl;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.redirect-url}")
    private String redirectUrl;

    @GetMapping
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect(naverUrl + "/oauth2.0/authorize" + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrl + "&state=STATE_STRING");
    }
}
