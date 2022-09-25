package ccc.keeweapi.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.OnboardRequest;
import ccc.keeweapi.dto.user.OnboardResponse;
import ccc.keeweapi.service.user.ProfileApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileApiService profileApiService;

    @PostMapping
    public ApiResponse<OnboardResponse> onboard(@RequestBody @Valid OnboardRequest request) {
        return ApiResponse.ok(profileApiService.onboard(request));
    }
}
