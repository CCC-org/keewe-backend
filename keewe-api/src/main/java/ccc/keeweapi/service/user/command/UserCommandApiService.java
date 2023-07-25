package ccc.keeweapi.service.user.command;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.service.user.command.UserCommandDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandApiService {
    private final UserCommandDomainService userCommandDomainService;

    public void registerPushToken(String pushToken) {
        userCommandDomainService.registerPushToken(SecurityUtil.getUserId(), pushToken);
    }
}
