package ccc.keewecore.exception;

import ccc.keewecore.consts.KeeweRtnConsts;
import lombok.Getter;

@Getter
public class KeeweException extends RuntimeException{

    private KeeweRtnConsts keeweRtnConsts;

    public KeeweException(KeeweRtnConsts keeweRtnConsts) {
        super(keeweRtnConsts.getDescription());
        this.keeweRtnConsts = keeweRtnConsts;
    }
}
