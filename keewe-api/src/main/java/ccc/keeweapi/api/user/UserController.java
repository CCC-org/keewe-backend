package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtUtils jwtUtils;

    @PostMapping("/api/v1/user")
    public String tempLogin(String email) {
        List<String> roles = new ArrayList<>();
        return jwtUtils.createToken(email, roles);
    }
}
