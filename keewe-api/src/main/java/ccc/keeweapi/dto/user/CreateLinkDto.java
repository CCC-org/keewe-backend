package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateLinkDto {
    private Long profileId;
    private String link;
}
