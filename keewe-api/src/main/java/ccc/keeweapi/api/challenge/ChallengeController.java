package ccc.keeweapi.api.challenge;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.challenge.ChallengeCreateRequest;
import ccc.keeweapi.dto.challenge.ChallengeParticipateRequest;
import ccc.keeweapi.service.challenge.ChallengeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/challenge")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeApiService challengeApiService;

    @PostMapping
    public ApiResponse<?> create(@RequestBody @Valid ChallengeCreateRequest request) {
        return ApiResponse.ok(challengeApiService.createChallenge(request));
    }

    @PostMapping(value = "/paticipate")
    public ApiResponse<?> participate(@RequestBody @Valid ChallengeParticipateRequest request) {
        return ApiResponse.ok(challengeApiService.participate(request));
    }
}
