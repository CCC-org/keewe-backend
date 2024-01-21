package ccc.keewedomain.persistence.domain.title.enums;

import lombok.Getter;

@Getter
public enum BookmarkTitle {
    북마크_최초(7000L, 1L);

    private final Long id;
    private final Long standard;

    BookmarkTitle(Long id, Long standard) {
        this.id = id;
        this.standard = standard;
    }
}
