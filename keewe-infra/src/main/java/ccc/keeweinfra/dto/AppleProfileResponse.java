package ccc.keeweinfra.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppleProfileResponse implements OauthResponse {
    private String id;
    private String email;

    @JsonCreator
    public AppleProfileResponse(
            @JsonProperty("id") String id,
            @JsonProperty("email") String email
    ) {
        this.id = id;
        this.email = email;
    }
}
