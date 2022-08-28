package ccc.keewedomain.dto.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChallengeCreateDto {
    private Long userId;
    private String name;
    private String interest;
    private String introduction;

    public static ChallengeCreateDto of(Long userId, String name, String interest, String introduction) {
        ChallengeCreateDto dto = new ChallengeCreateDto();
        dto.userId = userId;
        dto.interest = interest;
        dto.name = name;
        dto.introduction = introduction;

        return dto;
    }
}
