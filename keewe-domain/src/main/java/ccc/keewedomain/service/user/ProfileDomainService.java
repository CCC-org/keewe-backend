package ccc.keewedomain.service.user;

import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.user.OnboardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileDomainService {
    private final UserDomainService userDomainService;

    public User onboard(OnboardDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        user.initProfile(dto.getNickname(), dto.getInterests());
        return user;
    }
}
