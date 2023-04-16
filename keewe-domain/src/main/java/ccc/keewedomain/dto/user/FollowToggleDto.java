package ccc.keewedomain.dto.user;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FollowToggleDto {
    private Long userId;
    private Long targetId;

    public void validateSelfFollowing(FollowToggleDto followDto) {
        if(Objects.equals(followDto.getTargetId(), followDto.getUserId())) {
            throw new KeeweException(KeeweRtnConsts.ERR446);
        }
    }
}
