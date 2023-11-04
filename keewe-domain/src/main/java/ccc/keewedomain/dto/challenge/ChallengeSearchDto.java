package ccc.keewedomain.dto.challenge;

import ccc.keewedomain.dto.SearchDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ChallengeSearchDto implements SearchDto {
    private Long id;
    private String name;
    private String introduction;
    private String interestName;
    private Long insightCnt;
}
