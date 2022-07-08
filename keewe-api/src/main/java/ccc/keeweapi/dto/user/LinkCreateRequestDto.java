package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkCreateRequestDto {
    private Long profileId;
    private String link;
}
