package ccc.keewedomain.domain.common.enums;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LinkType {
    NAVER_BLOG("blog.naver.com"),
    FACEBOOK("facebook.com"),
    INSTAGRAM("instagram.com"),
    BRUNCH("brunch.co.kr"),
    LINKEDIN("linkedin.com"),
    GITHUB("github.com"),
    YOUTUBE("youtube.com"),
    BEHANCE("behance.net"),
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
