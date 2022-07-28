package ccc.keeweapi.dto.nest;

import ccc.keeweapi.validator.annotations.Enum;
import ccc.keeweapi.validator.annotations.PostContent;
import ccc.keewedomain.domain.nest.enums.PostType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "postType",
        visible = true)
@JsonSubTypes({
        @Type(value = VotePostCreateRequest.class, name = "VOTE"),
        @Type(value = FootprintPostCreateRequest.class, name = "FOOTPRINT")
})
@NoArgsConstructor
public class PostCreateRequest {
    private Long profileId;

    @PostContent
    private String content;

    @Enum(enumClass = PostType.class)
    private String postType;
}
