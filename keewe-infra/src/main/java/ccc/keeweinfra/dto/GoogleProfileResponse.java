package ccc.keeweinfra.dto;

import ccc.keeweinfra.vo.google.GoogleAccount;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleProfileResponse {
    private String id;
    private GoogleAccount googleAccount;

    @JsonCreator
    public GoogleProfileResponse(
            @JsonProperty("id") String id,
            @JsonProperty("email") String email
    ) {
        this.id = id;
        this.googleAccount = new GoogleAccount(email);
    }
}
