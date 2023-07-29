package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.RegisterPushTokenRequest;
import ccc.keeweapi.service.user.command.UserCommandApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/token")
@RequiredArgsConstructor
public class UserTokenController {
    private final UserCommandApiService userCommandApiService;

    @PutMapping("/push")
    public ApiResponse<Void> registerPushToken(@RequestBody RegisterPushTokenRequest request) {
        userCommandApiService.registerPushToken(request.getPushToken());
        return ApiResponse.ok();
    }
}
