package ccc.keeweapi.dto.user;

import lombok.Data;

@Data
public class NicknameCreateRequestDto {
    private Long profileId;
    private String nickname;
}
