package ccc.keeweapi.dto.insight;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DrawerUpdateRequest {
    @GraphemeLength(min = 1, max = 15)
    private String name;

    @JsonProperty("name")
    private void setName(String name) {
        this.name = name.trim();
    }
}
