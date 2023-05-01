package ccc.keeweapi.dto.insight.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReactionAggregationResponse {
    private Long clap;
    private Long heart;
    private Long sad;
    private Long surprise;
    private Long fire;
    private Long eyes;
}
