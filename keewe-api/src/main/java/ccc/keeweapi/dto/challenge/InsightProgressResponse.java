package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class InsightProgressResponse {
    private String name;
    private Long current;
    private Long total;
}
