package ccc.keewecore.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeeweRtnConsts {
    NRM000(KeeweRtnGrp.Success, 200, "성공"),

    ERR400(KeeweRtnGrp.Validation, 400, "유효하지 않은 값이에요."),

    ERR401(KeeweRtnGrp.Auth, 401, "토큰이 유효하지 않아요."),
    ERR402(KeeweRtnGrp.Auth, 402, "토큰이 만료됐어요."),
    ERR403(KeeweRtnGrp.Auth, 403, "토큰이 누락됐어요."),
    ERR404(KeeweRtnGrp.Auth, 404, "권한이 없는 유저입니다."),
    ERR405(KeeweRtnGrp.Validation, 405, "지원하지 않는 HTTP Method에요."),
    ERR406(KeeweRtnGrp.Validation, 406, "온보딩이 필요한 유저에요."),

    ERR411(KeeweRtnGrp.Validation, 411, "유저를 찾을 수 없어요."),
    ERR412(KeeweRtnGrp.Validation, 412, "유저가 이메일 제공에 동의를 하지 않았어요."),
    ERR413(KeeweRtnGrp.Validation, 413, "유저 토큰이 존재하지 않아요."),

    ERR420(KeeweRtnGrp.Validation, 420, "닉네임의 길이가 초과됐어요."),
    ERR421(KeeweRtnGrp.Validation, 421, "닉네임이 비어있어요."),
    ERR422(KeeweRtnGrp.Validation, 422, "프로필을 찾을 수 없어요."),
    ERR423(KeeweRtnGrp.Validation, 423, "링크 타입이 일치하지 않아요."),
    ERR424(KeeweRtnGrp.Validation, 424, "유효하지 않은 URL 입니다."),
    ERR425(KeeweRtnGrp.Validation, 425, "도메인과 링크 타입이 일치하지 않습니다."),
    ERR426(KeeweRtnGrp.Validation, 426, "소셜 링크의 개수가 초과됐어요."),
    ERR427(KeeweRtnGrp.Validation, 427, "온보딩이 완료된 유저에요."),
    ERR428(KeeweRtnGrp.Validation, 428, "인사이트를 통한 팔로우 기록이 이미 존재해요."),
    ERR429(KeeweRtnGrp.Validation, 429, "존재하지 않는 유저예요."),

    ERR430(KeeweRtnGrp.Validation, 430, "챌린지를 찾을 수 없어요."),
    ERR431(KeeweRtnGrp.Validation, 431, "이미 챌린지에 참여중이에요."),
    ERR432(KeeweRtnGrp.Validation, 432, "참가중인 챌린지가 없어요."),
    ERR433(KeeweRtnGrp.Validation, 433, "종료일을 오늘 이전으로 설정할 수 없어요."),
    ERR434(KeeweRtnGrp.Validation, 434, "달성 불가능한 기록 횟수로 변경할 수 없어요."),

    ERR440(KeeweRtnGrp.Validation, 440, "서랍을 찾을 수 없어요"),
    ERR441(KeeweRtnGrp.Validation, 441, "이미 등록된 서랍 이름이에요"),
    ERR442(KeeweRtnGrp.Validation, 442, "댓글을 찾을 수 없어요"),
    ERR443(KeeweRtnGrp.Validation, 443, "답글은 부모 댓글이 될 수 없어요."),
    ERR444(KeeweRtnGrp.Validation, 444, "서랍의 주인이 아니에요."),
    ERR445(KeeweRtnGrp.Validation, 445, "인사이트를 찾을 수 없어요."),
    ERR446(KeeweRtnGrp.Validation, 446, "자기 자신을 팔로잉 할 수 없어요."),
    ERR447(KeeweRtnGrp.Validation, 447, "인사이트 작성자만 삭제 가능해요."),
    ERR448(KeeweRtnGrp.Validation, 448, "댓글 작성자만 삭제 가능해요."),
    ERR449(KeeweRtnGrp.Validation, 449, "이미지 형식이 올바르지 않습니다."),

    ERR450(KeeweRtnGrp.Validation, 450, "이미 차단한 유저에요."),
    ERR451(KeeweRtnGrp.Validation, 451, "자신을 차단할 수 없어요."),
    ERR452(KeeweRtnGrp.Validation, 452, "차단 내역을 찾을 수 없어요."),
    ERR453(KeeweRtnGrp.Validation, 453, "차단한 유저에 대해 요청을 할 수 없어요."),

    ERR460(KeeweRtnGrp.Validation, 460, "인사이트의 작성자가 아니에요."),

    ERR471(KeeweRtnGrp.Validation, 471, "관련된 반응 통계를 찾을 수 없어요."),

    ERR480(KeeweRtnGrp.Validation, 480, "타이틀을 획득하지 않았어요."),
    ERR481(KeeweRtnGrp.Validation, 481, "댓글을 찾을 수 없어요."),
    ERR482(KeeweRtnGrp.Validation, 482, "리액션을 찾을 수 없어요."),
    ERR483(KeeweRtnGrp.Validation, 483, "알림을 찾을 수 없어요."),
    ERR484(KeeweRtnGrp.Validation, 484, "존재하지 않는 글이에요."),

    ERR490(KeeweRtnGrp.Validation, 490, "이미 인사이트를 통한 프로필 방문 기록이 존재해요."),

    ERR501(KeeweRtnGrp.System, 501, "카카오 회원가입 중 내부 오류가 발생했어요."),
    ERR502(KeeweRtnGrp.System, 502, "네이버 회원가입 중 내부 오류가 발생했어요."),
    ERR503(KeeweRtnGrp.System, 503, "네이버 회원가입 중 state가 일치하지 않아요"),
    ERR504(KeeweRtnGrp.Validation, 504, "지원하지 않는 소셜 타입이에요."),
    ERR505(KeeweRtnGrp.System, 505, "구글 회원가입 중 내부 오류가 발생했어요."),
    ERR506(KeeweRtnGrp.Validation, 506, "지원하지 않는 게시글 타입이에요."),
    ERR507(KeeweRtnGrp.System, 507, "잠시 후 다시 시도해주세요."),
    ERR510(KeeweRtnGrp.System, 510, "애플 회원가입 중 내부 오류가 발생했어요."),

    ERR600(KeeweRtnGrp.System, 600, "파일 업로드 중 문제가 발생했어요."),

    ERR999(KeeweRtnGrp.System, 999, "처리되지 않은 예외가 발생했어요.");

    private KeeweRtnGrp grp;
    private int code;
    private String description;
}
