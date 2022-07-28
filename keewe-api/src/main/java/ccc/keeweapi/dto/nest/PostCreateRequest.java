package ccc.keeweapi.dto.nest;

import ccc.keeweapi.validator.annotations.Enum;
import ccc.keeweapi.validator.annotations.PostContent;
import ccc.keewedomain.domain.nest.enums.PostType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "postType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VotePostCreateRequest.class, name = "VOTE"),
        @JsonSubTypes.Type(value = FootprintPostCreateRequest.class, name = "FOOTPRINT")
})
@NoArgsConstructor
public class PostCreateRequest {
    private Long profileId;
    @PostContent
    private String content;

    public String getPostType() {
        return postType;
    }

    @Enum(enumClass = PostType.class)
    private String postType;
}
