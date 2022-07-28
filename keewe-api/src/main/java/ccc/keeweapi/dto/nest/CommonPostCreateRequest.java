package ccc.keeweapi.dto.nest;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;


@Data
@JsonDeserialize(as = CommonPostCreateRequest.class)
public class CommonPostCreateRequest implements AbstractPostCreateRequest {
    private Long profileId;
  //  @PostContent
    private String content;
    private String postType;
}
