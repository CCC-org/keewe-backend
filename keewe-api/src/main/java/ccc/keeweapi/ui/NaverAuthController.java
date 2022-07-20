package ccc.keeweapi.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    public NaverAuthController() throws NoSuchAlgorithmException {
        this.secureRandom = SecureRandom.getInstance("SHA1PRNG");
    }

    @GetMapping
    public void redirect(HttpSession session, HttpServletResponse response) throws IOException {
        String state = generateState();
        session.setAttribute(naverState, state);
        response.sendRedirect(naverUrl + "/oauth2.0/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrl + "&state=" + state);
    }

    private String generateState() {
        int stateInt = Math.abs(secureRandom.nextInt());
        return Integer.toString(stateInt);
    }
}
