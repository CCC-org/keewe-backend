package ccc.keeweapi.dto.insight.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DrawerResponse {

    private Long id;
    private String name;

    public static DrawerResponse of(Long id, String name) {
        DrawerResponse response = new DrawerResponse();
        response.id = id;
        response.name = name;

        return response;
    }
}
