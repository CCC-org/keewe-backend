package ccc.keeweapi.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowerListResponse {
    private String cursor;
    private List<FollowerResponse> followers;

    public static FollowerListResponse of(Optional<LocalDateTime> cursor, List<FollowerResponse> followers) {
        FollowerListResponse response = new FollowerListResponse();
        response.cursor = cursor.map(LocalDateTime::toString).orElse(null);
        response.followers = followers;

        return response;
    }
}
