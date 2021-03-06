package ccc.keeweapi.dto.user;

import ccc.keeweapi.dto.common.LinkDto;
import lombok.Data;

import java.util.List;

@Data
public class SocialLinkCreateRequest {
    private Long profileId;
    private List<LinkDto> links;
}
