package ccc.keewedomain.domain.common;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.common.enums.LinkType;
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

    public static Link of(String url, String typeStr) {
        LinkType type = LinkType.valueOfOrElseThrow(typeStr);
        checkUrlTypeOrElseThrow(url, type);
        Link link = new Link();
        link.url = url;
        link.type = type;

        return link;
    }

    private static void checkUrlTypeOrElseThrow(String url, LinkType type) {
        URL urlObj;
        try {
            url = url.replaceFirst("www\\.", "");
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            throw new KeeweException(KeeweRtnConsts.ERR424);
        }

        if(!urlObj.getHost().equals(type.getDomain())) {
            throw new KeeweException(KeeweRtnConsts.ERR425);
        }
    }
}
