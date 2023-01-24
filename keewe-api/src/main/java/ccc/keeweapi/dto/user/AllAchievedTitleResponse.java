package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class AllAchievedTitleResponse {
    private Long repTitleId;
    private List<AchievedTitleResponse> achievedTitles;
}
