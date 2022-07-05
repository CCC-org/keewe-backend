package ccc.keeweapi.consts;

public enum KeeweRtnGrp {
    Success(200),
    Auth(400),
    Validation(410),
    System(500);

    private int repCode;

    KeeweRtnGrp(int repCode) {
        this.repCode = repCode;
    }
}
