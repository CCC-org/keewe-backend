package ccc.keeweapi.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ParticipationProgressResponse {
    private String name;
    private Long current;
    private Long total;
}
