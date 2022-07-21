package ccc.keeweapi.dto.user;

import ccc.keewedomain.domain.user.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class LinkCreateResponse {
    private String link;
    private ProfileStatus status;
}
