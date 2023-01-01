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
    private List<FollowUserResponse> followers;

    public static FollowUserListResponse of(Optional<LocalDateTime> cursor, List<FollowUserResponse> followers) {
        FollowUserListResponse response = new FollowUserListResponse();
        response.cursor = cursor.map(LocalDateTime::toString).orElse(null);
        response.followers = followers;

        return response;
    }
}
