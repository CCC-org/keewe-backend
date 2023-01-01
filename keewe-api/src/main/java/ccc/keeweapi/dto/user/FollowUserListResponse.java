package ccc.keeweapi.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowUserListResponse {
    private String cursor;
    private List<FollowUserResponse> users;

    public static FollowUserListResponse of(Optional<LocalDateTime> cursor, List<FollowUserResponse> users) {
        FollowUserListResponse response = new FollowUserListResponse();
        response.cursor = cursor.map(LocalDateTime::toString).orElse(null);
        response.users = users;

        return response;
    }
}
