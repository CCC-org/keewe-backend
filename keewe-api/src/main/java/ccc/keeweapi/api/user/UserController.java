package ccc.keeweapi.api.user;

import ccc.keeweapi.config.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtUtils jwtUtils;

    @PostMapping
    public String tempLogin(@RequestBody String email) {
        System.out.println("email = " + email);
        List<String> roles = new ArrayList<>();
        return jwtUtils.createToken(email, roles);
    }
}
