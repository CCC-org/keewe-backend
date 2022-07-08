package ccc.keeweapi.dto.user;

import ccc.keewedomain.domain.user.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LinkCreateResponseDto {
    private String link;
    private ProfileStatus status;
}
