package ccc.keeweapi.dto.user;

import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class IncompleteProfileResponse {

    private boolean exist;
    private Long profileId;
    private ProfileStatus status;

    public static IncompleteProfileResponse getExistResult(Profile profile) {
        return new IncompleteProfileResponse(true, profile.getId(), profile.getProfileStatus());
    }

    public static IncompleteProfileResponse getNotExistResult() {
        IncompleteProfileResponse incompleteProfileResponse = new IncompleteProfileResponse();
        incompleteProfileResponse.exist = false;
        return incompleteProfileResponse;
    }

}
