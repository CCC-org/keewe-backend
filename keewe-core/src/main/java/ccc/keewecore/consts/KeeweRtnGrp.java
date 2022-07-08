package ccc.keewecore.consts;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum KeeweRtnGrp {
    Success(200),
    Auth(400),
    Validation(410),
    System(500);

    private int repCode;
}
