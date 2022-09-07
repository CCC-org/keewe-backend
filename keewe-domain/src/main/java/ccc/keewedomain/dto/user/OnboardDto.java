package ccc.keewedomain.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnboardDto {
    private Long userId;
    private String nickname;
    private Set<String> interests;

    public static OnboardDto of(Long userId, String nickname, Set<String> interests) {
        OnboardDto dto = new OnboardDto();
        dto.userId = userId;
        dto.nickname = nickname;
        dto.interests = interests;

        return dto;
    }
}
