package ccc.keewedomain.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LinkType {
    YOUTUBE("youtube.com"),
    INSTAGRAM("instagram.com"),
    FACEBOOK("facebook.com"),
    OTHER("");


    private String domain;
}
