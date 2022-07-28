package ccc.keeweapi.dto.nest;

import ccc.keeweapi.dto.nest.handler.PostDeserializeHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonDeserialize(using = PostDeserializeHandler.class)
public interface AbstractPostCreateRequest {

    String getPostType();
}

