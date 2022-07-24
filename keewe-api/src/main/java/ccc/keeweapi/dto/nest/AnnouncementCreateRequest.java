package ccc.keeweapi.dto.nest;

import lombok.Data;

@Data
public class AnnouncementCreateRequest {
    private Long profileId;
    @PostContent
    private String content;
}
