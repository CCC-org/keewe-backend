package ccc.keewedomain.persistence.domain.title.enums;

import lombok.Getter;

@Getter
public enum ReactionTitle {

    리엑션_최초(4000L, 1L);

    private final Long id;
    private final Long standard;

    ReactionTitle(Long id, Long standard) {
        this.id = id;
        this.standard = standard;
    }
}
