package ccc.keewedomain.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProfileStatus {
    NICKNAME_NEEDED(1),
    LINK_NEEDED(2),
    ACTIVITIES_NEEDED(3),
    SOCIAL_LINK_NEEDED(4),
    ACTIVE(100);

    private final int order;
}
