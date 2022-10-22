package ccc.keewedomain.persistence.repository.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CursorPageable<T> {

    private T cursor;
    private Long limit;

    public static <T> CursorPageable<T> of(T cursor, Long limit) {
        return new CursorPageable<>(cursor, limit);
    }
}
