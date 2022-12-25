package ccc.keeweapi.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AchievedTitleResponse {
    private Long titleId;
    private String name;
    private String introduction;
    private String achievedDate;

    public static AchievedTitleResponse of(Long titleId, String name, String introduction, LocalDateTime achievedDate) {
        AchievedTitleResponse response = new AchievedTitleResponse();
        response.titleId = titleId;
        response.name = name;
        response.introduction = introduction;
        response.achievedDate = achievedDate.toString();

        return response;
    }
}
