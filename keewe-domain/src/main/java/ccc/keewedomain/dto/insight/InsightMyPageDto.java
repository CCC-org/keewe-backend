package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.common.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
public class InsightMyPageDto {
    private Long id;
    private String contents;
    private Link link;
    private ReactionAggregationGetDto reaction;
    private LocalDateTime createdAt;
    private boolean isBookmark;
}
