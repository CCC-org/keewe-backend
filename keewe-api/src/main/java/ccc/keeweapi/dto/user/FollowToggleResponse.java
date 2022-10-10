package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor(staticName = "of")
@Getter
public class FollowToggleResponse {
    private boolean following;
}