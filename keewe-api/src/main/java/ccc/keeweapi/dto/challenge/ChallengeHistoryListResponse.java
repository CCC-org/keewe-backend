package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class ChallengeHistoryListResponse {
    private Long historyNumber;
    private List<ChallengeHistoryResponse> challengeHistories;
}
