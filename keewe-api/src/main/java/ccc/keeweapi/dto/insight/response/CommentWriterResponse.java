package ccc.keeweapi.dto.insight.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CommentWriterResponse {

    private Long id;
    private String name;
    private String title;
    private String image;
}
