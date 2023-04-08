package ccc.keeweapi.dto.insight;

import ccc.keeweapi.dto.BlockFilteringResponse;
import ccc.keewedomain.dto.insight.InsightWriterDto;
import ccc.keewedomain.persistence.domain.common.Link;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InsightGetForHomeResponse implements BlockFilteringResponse {
    private Long id;
    private String contents;
    private boolean isBookmark;
    private Link link;
    private ReactionAggregationResponse reaction;
    private String createdAt;
    private InsightWriterDto writer;

    @Override
    public Long userId() {
        return writer.getWriterId();
    }
}
