package ccc.keeweapi.dto.user;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import ccc.keewecore.consts.KeeweRegexs;
import ccc.keewecore.utils.KeeweStringUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class OnboardRequest {
    @GraphemeLength(min = 1, max = 12)
    private String nickname;

    @NotNull
    @Size(min = 1, max = 5)
    private List<@Size(min = 1, max = 8) @Pattern(regexp = KeeweRegexs.KOREAN_OR_ENGLISH) String> interests;

    @JsonProperty("nickname")
    private void setNickname(String nickname) {
        this.nickname = KeeweStringUtil.compressWhiteSpaces(nickname);
    }
}
