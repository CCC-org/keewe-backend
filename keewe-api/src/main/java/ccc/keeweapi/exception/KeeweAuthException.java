package ccc.keeweapi.exception;


import ccc.keeweapi.consts.KeeweRtnConsts;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class KeeweAuthException extends AuthenticationException {

    public KeeweAuthException(KeeweRtnConsts keeweRtnConsts) {
        super(keeweRtnConsts.getDescription());
        this.keeweRtnConsts = keeweRtnConsts;
    }

    private KeeweRtnConsts keeweRtnConsts;
}
