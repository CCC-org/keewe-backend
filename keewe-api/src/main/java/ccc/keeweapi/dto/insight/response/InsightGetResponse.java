package ccc.keeweapi.dto.insight.response;

import ccc.keewedomain.persistence.domain.common.Link;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightGetResponse {
    private Long id;
    private String contents;
    private Link link;
    private ReactionAggregationResponse reaction;
    private boolean isBookmark;
    private Long drawerId;
    private String drawerName;
}
