package ccc.keewedomain.dto.insight;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class InsightWriterDto {
    private Long writerId;
    private String nickname;
    private String title;
    private String image;
}
