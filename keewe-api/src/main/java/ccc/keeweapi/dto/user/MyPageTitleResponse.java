package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class MyPageTitleResponse {
    private Long total;
    private List<AchievedTitleResponse> achievedTitles;
}
