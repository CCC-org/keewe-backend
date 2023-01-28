package ccc.keewedomain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@AllArgsConstructor(staticName = "of")
public class ProfileUpdateDto {
    private MultipartFile profileImage;
    private String nickname;
    private Set<String> interests;
    private Long repTitleId;
    private String introduction;
    private Boolean deletePhoto;
}
