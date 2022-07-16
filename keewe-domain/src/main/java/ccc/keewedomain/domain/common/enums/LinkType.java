package ccc.keewedomain.domain.common.enums;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
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

    public static LinkType valueOfOrElseThrow(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException ex) {
            throw new KeeweException(KeeweRtnConsts.ERR423);
        }
    }
}
