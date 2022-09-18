package ccc.keewedomain.dto.insight;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DrawerCreateDto {
    private Long userId;
    private String name;

    public static DrawerCreateDto of(Long userId, String name) {
        DrawerCreateDto dto = new DrawerCreateDto();
        dto.userId = userId;
        dto.name = name;

        return dto;
    }
}
