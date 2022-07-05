package ccc.keeweapi.service;

import ccc.keeweapi.config.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtils jwtUtils;

    public String getToken() {
        return jwtUtils.createToken("hoseong", List.of());
    }
}
