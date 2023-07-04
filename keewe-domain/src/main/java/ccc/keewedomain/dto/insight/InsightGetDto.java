package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.common.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class InsightGetDto {
    private Long id;
    private String contents;
    private Link link;
    private ReactionAggregationGetDto reaction;
    private boolean isBookmark;
    private Long drawerId;
    private String drawerName;
}
