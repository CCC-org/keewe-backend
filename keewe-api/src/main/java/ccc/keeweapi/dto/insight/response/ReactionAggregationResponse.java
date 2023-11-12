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
    private Boolean isClapClicked;
    private Long heart;
    private Boolean isHeartClicked;
    private Long sad;
    private Boolean isSadClicked;
    private Long surprise;
    private Boolean isSurpriseClicked;
    private Long fire;
    private Boolean isFireClicked;
    private Long eyes;
    private Boolean isEyesClicked;

    public static ReactionAggregationResponse of(
        Long clap,
        Long heart,
        Long sad,
        Long surprise,
        Long fire,
        Long eyes
    ) {
        return ReactionAggregationResponse.of(
                clap,
                false,
                heart,
                false,
                sad,
                false,
                surprise,
                false,
                fire,
                false,
                eyes,
                false
        );
    }
}
