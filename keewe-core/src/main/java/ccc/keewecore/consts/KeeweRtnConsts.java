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

    ERR400(KeeweRtnGrp.Validation, 400, "유효하지 않은 값이에요."),

    ERR411(KeeweRtnGrp.Validation, 411, "유저를 찾을 수 없어요."),
    ERR412(KeeweRtnGrp.Validation, 412, "유저가 이메일 제공에 동의를 하지 않았어요."),

    ERR420(KeeweRtnGrp.Validation, 420, "닉네임의 길이가 초과됐어요."),
    ERR421(KeeweRtnGrp.Validation, 421, "닉네임이 비어있어요."),
    ERR422(KeeweRtnGrp.Validation, 422, "프로필을 찾을 수 없어요."),
    ERR423(KeeweRtnGrp.Validation, 423, "링크 타입이 일치하지 않아요."),
    ERR424(KeeweRtnGrp.Validation, 424, "유효하지 않은 URL 입니다."),
    ERR425(KeeweRtnGrp.Validation, 425, "도메인과 링크 타입이 일치하지 않습니다."),
    ERR426(KeeweRtnGrp.Validation, 426, "소셜 링크의 개수가 초과됐어요."),
    ERR427(KeeweRtnGrp.Validation, 427, "온보딩이 완료된 유저에요."),

    ERR430(KeeweRtnGrp.Validation, 430, "챌린지를 찾을 수 없어요."),
    ERR431(KeeweRtnGrp.Validation, 431, "이미 챌린지에 참여중이에요."),
    ERR432(KeeweRtnGrp.Validation, 432, "참가중인 챌린지가 없어요"),

    ERR440(KeeweRtnGrp.Validation, 440, "서랍을 찾을 수 없어요"),
    ERR441(KeeweRtnGrp.Validation, 441, "이미 등록된 서랍 이름이에요"),
    ERR442(KeeweRtnGrp.Validation, 442, "댓글을 찾을 수 없어요"),
    ERR443(KeeweRtnGrp.Validation, 443, "답글은 부모 댓글이 될 수 없어요."),
    ERR444(KeeweRtnGrp.Validation, 444, "서랍의 주인이 아니에요."),
    ERR445(KeeweRtnGrp.Validation, 445, "인사이트를 찾을 수 없어요."),
    ERR446(KeeweRtnGrp.Validation, 446, "자기 자신을 팔로잉 할 수 없어요."),

    ERR471(KeeweRtnGrp.Validation, 471, "관련된 반응 통계를 찾을 수 없어요."),

    ERR501(KeeweRtnGrp.System, 501, "카카오 회원가입 중 내부 오류가 발생했어요."),
    ERR502(KeeweRtnGrp.System, 502, "네이버 회원가입 중 내부 오류가 발생했어요."),
    ERR503(KeeweRtnGrp.System, 503, "네이버 회원가입 중 state가 일치하지 않아요"),
    ERR504(KeeweRtnGrp.System, 504, "지원하지 않는 소셜 타입이에요."),
    ERR505(KeeweRtnGrp.System, 505, "구글 회원가입 중 내부 오류가 발생했어요."),

    ERR506(KeeweRtnGrp.System, 506, "지원하지 않는 게시글 타입이에요."),

    ERR900(KeeweRtnGrp.System, 900, "캐시 키 매핑과정에서 에러가 발생했어요."),
    ERR999(KeeweRtnGrp.System, 999, "처리되지 않은 예외가 발생했어요.");

    private KeeweRtnGrp grp;
    private int code;
    private String description;
}
