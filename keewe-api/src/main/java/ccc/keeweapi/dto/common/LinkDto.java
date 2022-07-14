package ccc.keeweapi.dto.common;

import ccc.keewedomain.domain.common.enums.LinkType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LinkDto {
    private String url;
    private LinkType type;

    @JsonCreator
    public LinkDto(@JsonProperty("url") String url, @JsonProperty("type") String type) {
        this.url = url;
        try {
            this.type = LinkType.valueOf(type);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(String.format("[%s]에 해당하는 LinkType이 존재하지 않습니다.", type));
        }
    }
}
