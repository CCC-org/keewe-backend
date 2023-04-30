package ccc.keeweapi.dto.insight.request;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DrawerCreateRequest {
    @GraphemeLength(min = 1, max = 15)
    private String name;

    @JsonProperty("name")
    private void setName(String name) {
        this.name = name.trim();
    }
}
