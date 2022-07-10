package ccc.keewecore.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeeweRtnConsts {
    NRM000(KeeweRtnGrp.Success, 200, "성공"),

    ERR401(KeeweRtnGrp.Auth, 401, "토큰이 유효하지 않아요."),
    ERR402(KeeweRtnGrp.Auth, 402, "토큰이 만료됐어요."),
    ERR403(KeeweRtnGrp.Auth, 403, "토큰이 누락됐어요."),

    ERR400(KeeweRtnGrp.Validation, 400, "잘못된 요청이에요."),
    ERR411(KeeweRtnGrp.Validation, 411, "유저를 찾을 수 없어요.");

    private KeeweRtnGrp grp;
    private int code;
    private String description;
}
