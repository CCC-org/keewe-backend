package ccc.keeweapi.dto.user;

import ccc.keeweapi.validator.annotations.GraphemeLength;
import ccc.keewecore.consts.KeeweRegexs;
import ccc.keewecore.utils.KeeweStringUtils;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

//@Data
@Getter
public class ProfileUpdateRequest {
    private MultipartFile profileImage;

    @GraphemeLength(min = 1, max = 12)
    private String nickname;

    private Long repTitleId;

    private String introduction;

    private Boolean deletePhoto;

    @NotNull
    @Size(min = 1, max = 5)
    private Set<@Size(min = 1, max = 8) @Pattern(regexp = KeeweRegexs.KOREAN_OR_ENGLISH) String> interests;

    public ProfileUpdateRequest(MultipartFile profileImage,
                                String nickname,
                                Long repTitleId,
                                String introduction,
                                Set<String> interests,
                                Boolean deletePhoto) {
        this.profileImage = profileImage;
        this.nickname = KeeweStringUtils.compressWhiteSpaces(nickname);
        this.repTitleId = repTitleId;
        this.introduction = introduction;
        this.interests = interests;
        this.deletePhoto = deletePhoto != null;
    }
}
