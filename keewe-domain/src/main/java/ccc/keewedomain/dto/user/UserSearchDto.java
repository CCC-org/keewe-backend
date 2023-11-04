package ccc.keewedomain.dto.user;

import ccc.keewedomain.dto.SearchDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class UserSearchDto implements SearchDto {
    private Long id;
    private String nickname;
    private String imageURL;
    private String title;
    private boolean isFollow;
}
