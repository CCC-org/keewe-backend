package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.common.Link;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InsightGetForHomeDto {
    private Long id;
    private String contents;
    private boolean isBookmark;
    private Link link;
    private ReactionAggregationGetDto reaction;
    private LocalDateTime createdAt;
    private InsightWriterDto writer;

    public InsightGetForHomeDto(Long id, String contents, boolean isBookmark, Link link, ReactionAggregationGetDto reaction, LocalDateTime createdAt, InsightWriterDto writer) {
        this.id = id;
        this.contents = contents;
        this.isBookmark = isBookmark;
        this.link = link;
        this.reaction = reaction;
        this.createdAt = createdAt;
        this.writer = writer;
    }

    public static InsightGetForHomeDto of(Long id, String contents, boolean isBookmark, Link link, ReactionAggregationGetDto reaction, LocalDateTime createdAt, InsightWriterDto writer) {
        return new InsightGetForHomeDto(id, contents, isBookmark, link, reaction, createdAt, writer);
    }
}
