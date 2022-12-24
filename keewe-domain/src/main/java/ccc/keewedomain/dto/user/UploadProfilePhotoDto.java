package ccc.keewedomain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor(staticName = "of")
public class UploadProfilePhotoDto {
    private Long userId;
    private MultipartFile imageFile;
}
