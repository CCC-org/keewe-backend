package ccc.keeweapi.dto.challenge;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import ccc.keewecore.consts.KeeweRegexs;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@ToString
public class ChallengeCreateRequest {
    @Size(min = 1, max = 8)
    @Pattern(regexp = KeeweRegexs.KOREAN_OR_ENGLISH)
    private String interest;

    @GraphemeLength(min = 1, max = 25)
    private String name;

    @GraphemeLength(max = 150)
    private String introduction;

    @Valid
    @NotNull
    private ChallengeParticipateRequest participation;
}
