package ccc.keeweapi.dto.user;

import lombok.Data;

@Data
public class NicknameCreateDto {
    private Long profileId;
    private String nickname;
}
