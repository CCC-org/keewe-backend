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
    ERR404(KeeweRtnGrp.Auth, 404, "권한이 없는 유저입니다."),

    ERR400(KeeweRtnGrp.Validation, 400, "잘못된 요청이에요."), // FIXME
    ERR411(KeeweRtnGrp.Validation, 411, "유저를 찾을 수 없어요."),
    ERR412(KeeweRtnGrp.Validation, 412, "유저가 이메일 제공에 동의를 하지 않았어요."),

    ERR420(KeeweRtnGrp.Validation, 420, "닉네임의 길이가 초과됐어요."),
    ERR421(KeeweRtnGrp.Validation, 421, "닉네임이 비어있어요."),
    ERR422(KeeweRtnGrp.Validation, 422, "프로필을 찾을 수 없어요."),
    ERR423(KeeweRtnGrp.Validation, 423, "링크 타입이 일치하지 않아요."),
    ERR424(KeeweRtnGrp.Validation, 424, "유효하지 않은 URL 입니다."),
    ERR425(KeeweRtnGrp.Validation, 425, "도메인과 링크 타입이 일치하지 않습니다."),
    ERR426(KeeweRtnGrp.Validation, 426, "소셜 링크의 개수가 초과됐어요."),

    ERR501(KeeweRtnGrp.System, 501, "카카오 회원가입 중 내부 오류가 발생했어요.");

    private KeeweRtnGrp grp;
    private int code;
    private String description;
}
