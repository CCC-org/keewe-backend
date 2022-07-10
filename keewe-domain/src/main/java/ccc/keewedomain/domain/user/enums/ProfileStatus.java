package ccc.keewedomain.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProfileStatus {
    LINK_NEEDED(1),
    ACTIVITIES_NEEDED(2),
    NICKNAME_NEEDED(3),
    SOCIAL_LINK_NEEDED(4),
    ACTIVE(100);

    private int order;
}
