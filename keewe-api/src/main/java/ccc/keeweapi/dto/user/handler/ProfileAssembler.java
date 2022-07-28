package ccc.keeweapi.dto.user.handler;

import ccc.keeweapi.dto.user.LinkCreateResponse;
import ccc.keeweapi.dto.user.NicknameCreateResponse;
import ccc.keewedomain.domain.user.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileAssembler {

    public LinkCreateResponse toLinkCreateResponse(Profile profile) {
        return LinkCreateResponse.of(profile.getLink(), profile.getProfileStatus());
    }

    public NicknameCreateResponse toNicknameCreateResponse(Profile profile) {
        return NicknameCreateResponse.of(profile.getNickname(), profile.getProfileStatus());
    }
}
