package ccc.keewedomain.domain.common;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.net.MalformedURLException;
import java.net.URL;

@Embeddable
@Getter
@Slf4j
public class Link {

    @Column(name = "url")
    private String url;

    public static Link of(String url) {
//        LinkType type = LinkType.valueOfOrElseThrow(typeStr);
//        checkUrlTypeOrElseThrow(url);
        Link link = new Link();
        link.url = url;

        return link;
    }


    //FIXME
    private static void checkUrlTypeOrElseThrow(String url) {
        URL urlObj;
        try {
            url = url.replaceFirst("www\\.", "");
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            throw new KeeweException(KeeweRtnConsts.ERR424);
        }

    }
}
