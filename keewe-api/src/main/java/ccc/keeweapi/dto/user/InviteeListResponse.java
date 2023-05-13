package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class InviteeListResponse {
    // 응답의 createdAt중 최소 값
    private String nextCursor;
    private List<InviteeResponse> invitees;
}
