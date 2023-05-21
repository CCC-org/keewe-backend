package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.common.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
public class InsightGetForBookmarkedDto {
    private Long id;
    private String contents;
    private boolean isBookmark;
    private Link link;
    private ReactionAggregationGetDto reaction;
    private LocalDateTime createdAt;
    private LocalDateTime bookmarkedAt;
    private InsightWriterDto writer;
}
