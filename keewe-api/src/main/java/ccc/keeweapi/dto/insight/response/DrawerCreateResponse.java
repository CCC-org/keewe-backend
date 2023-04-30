package ccc.keeweapi.dto.insight.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DrawerCreateResponse {
    private Long drawerId;

    public static DrawerCreateResponse of(Long drawerId) {
        DrawerCreateResponse response = new DrawerCreateResponse();
        response.drawerId = drawerId;

        return response;
    }
}
