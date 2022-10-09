package ccc.keewedomain.dto.insight;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class ReactionAggregationGetDto {
    private Long clap;
    private Long heart;
    private Long sad;
    private Long surprise;
    private Long fire;
    private Long eyes;
}
