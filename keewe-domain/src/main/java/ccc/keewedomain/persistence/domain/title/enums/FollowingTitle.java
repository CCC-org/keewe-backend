package ccc.keewedomain.persistence.domain.title.enums;

import lombok.Getter;

@Getter
public enum FollowingTitle {
    두근두근_첫만남(3000L, 1L),
    자타공인_인기인(3001L, 10L),
    피리부는_사나이(3002L, 100L)
    ;

    private final Long id;
    private final Long standard;

    FollowingTitle(Long id, Long standard) {
        this.id = id;
        this.standard = standard;
    }
}
