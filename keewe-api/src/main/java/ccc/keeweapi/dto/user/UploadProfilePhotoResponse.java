package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class UploadProfilePhotoResponse {
    private String image;
}
