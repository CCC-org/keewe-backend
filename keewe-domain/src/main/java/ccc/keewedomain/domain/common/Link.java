package ccc.keewedomain.domain.common;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.common.enums.LinkType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.net.MalformedURLException;
import java.net.URL;

@Embeddable
@Getter
@Slf4j
public class Link {

    @Column(name = "url")
    private String url;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private LinkType type;

    public static Link of(String url, LinkType type) {
        checkUrlTypeOrElseThrow(url, type);
        Link link = new Link();
        link.url = url;
        link.type = type;

        return link;
    }

    private static void checkUrlTypeOrElseThrow(String url, LinkType type) {
        URL urlObj;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            log.error("[{}]는 유효하지 않은 URL입니다.", url);
            throw new KeeweException(KeeweRtnConsts.ERR400);
        }

        if(!urlObj.getHost().equals(type.getDomain())) {
            log.error("[{}]는 [{}]의 도메인이 아닙니다.", urlObj.getHost(), type);
            throw new KeeweException(KeeweRtnConsts.ERR400);
        }
    }
}
