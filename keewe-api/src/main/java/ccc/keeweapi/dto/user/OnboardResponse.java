package ccc.keeweapi.dto.user;

import ccc.keewedomain.domain.common.Interest;
import lombok.Getter;

import java.util.List;

@Getter
public class OnboardResponse {
    private Long userId;
    private String nickname;
    private List<Interest> interests;

    public static OnboardResponse of(Long userId, String nickname, List<Interest> interests) {
        OnboardResponse response = new OnboardResponse();
        response.userId = userId;
        response.nickname = nickname;
        response.interests = interests;
        return response;
    }
}
