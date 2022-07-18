package ccc.keeweapi.dto.user;

import lombok.Data;

@Data
public class NicknameCreateRequest {
    private Long profileId;
    private String nickname;
}
