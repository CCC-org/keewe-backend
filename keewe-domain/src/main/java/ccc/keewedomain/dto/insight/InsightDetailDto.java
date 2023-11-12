package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InsightDetailDto {
    private Long userId;
    private Long insightId;
    private User user;

    public static InsightDetailDto of(Long userId, Long insightId, User user) {
        InsightDetailDto dto = new InsightDetailDto();
        dto.userId = userId;
        dto.insightId = insightId;
        dto.user = user;
        return dto;
    }
}
