package ccc.keewedomain.persistence.domain.title.enums;

import lombok.Getter;

@Getter
public enum ReactionTitle {

    리엑션_최초(4000L, 1L),
    리엑션_50회(4001L, 50L);

    private final Long id;
    private final Long standard;

    ReactionTitle(Long id, Long standard) {
        this.id = id;
        this.standard = standard;
    }
}
