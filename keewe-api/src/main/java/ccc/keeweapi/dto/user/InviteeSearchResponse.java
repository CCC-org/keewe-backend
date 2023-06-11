package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class InviteeSearchResponse {
    // 응답의 닉네임 중 사전순 가장 뒤
    private String nextCursor;
    private List<InviteeResponse> invitees;
}
