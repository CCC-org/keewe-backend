package ccc.keeweinfra.dto;

import ccc.keeweinfra.vo.Naver.NaverAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NaverProfileResponse {
    private String resultCode;
    private String message;
    @JsonProperty("response")
    private NaverAccount naverAccount;
}
