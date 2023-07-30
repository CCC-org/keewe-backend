package ccc.keeweapi.service.user.command;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.service.user.command.UserTokenCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandApiService {
    private final UserTokenCommandService userTokenCommandService;

    public void registerPushToken(String pushToken) {
        userTokenCommandService.registerPushToken(SecurityUtil.getUserId(), pushToken);
    }
}
