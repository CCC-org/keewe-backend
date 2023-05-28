package ccc.keeweapi.dto.insight.response;

import ccc.keeweapi.dto.BlockFilteringResponse;
import ccc.keewedomain.dto.insight.InsightWriterDto;
import ccc.keewedomain.persistence.domain.common.Link;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightGetForBookmarkedResponse implements BlockFilteringResponse {
    private Long id;
    private String contents;
    private boolean isBookmark;
    private Link link;
    private ReactionAggregationResponse reaction;
    private String createdAt;
    private String bookmarkedAt;
    private InsightWriterDto writer;

    @Override
    @JsonIgnore
    public Long getUserId() {
        return writer.getWriterId();
    }
}
