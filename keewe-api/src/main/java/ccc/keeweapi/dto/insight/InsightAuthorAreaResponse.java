package ccc.keeweapi.dto.insight;


import ccc.keewedomain.persistence.domain.common.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@Getter
public class InsightAuthorAreaResponse {
    private Long authorId;
    private String nickname;
    private String title;
    private List<Interest> interests;
    private String image;
    private boolean isAuthor;
    private String createdAt;
}
