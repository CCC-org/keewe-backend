package ccc.keewedomain.persistence.domain.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationContents {
    // title
    시작이반(NotificationCategory.TITLE, "타이틀 획득!", "키위에 어서오세요. 시작이 반!"),
    위대한_첫_도약(NotificationCategory.TITLE, "타이틀 획득!", "첫 인사이트를 올리셨네요. 위대한 첫 도약!"),
    초보_기록가(NotificationCategory.TITLE, "타이틀 획득!", "꾸준함이 중요하죠. 초보 기록가!"),
    중급_기록가(NotificationCategory.TITLE, "타이틀 획득!", "꾸준함으로 만든 10개의 글. 중급 기록가!"),
    고급_기록가(NotificationCategory.TITLE, "타이틀 획득!", "언제 50개나 쓰셨죠?! 고급 기록가!"),
    인사이트의_신(NotificationCategory.TITLE, "타이틀 획득!", "당신은 그저 God...인사이트의 신!"),
    혼자서도_잘_해요(NotificationCategory.TITLE, "타이틀 획득!", "알아서 척척 하는 당신은, 혼자서도 잘 해요!"),
    두근두근_첫만남(NotificationCategory.TITLE, "타이틀 획득!", "첫 팔로워라니...두근두근 첫만남!"),
    자타공인_인기인(NotificationCategory.TITLE, "타이틀 획득!", "너도나도 닮고싶은  당신. 자타공인 인기인!"),
    피리부는_사나이(NotificationCategory.TITLE, "타이틀 획득!", "100명이 쫓아다니는 인기인. 피리부는 사나이!"),
    정이_많은(NotificationCategory.TITLE, "타이틀 획득!", "40명을 마음에 담았네요. 정이 많은!"),
    참_잘했어요(NotificationCategory.TITLE, "타이틀 획득!", "처음 반응을 받았어요. 참 잘했어요!"),
    아낌없이_주는_나무(NotificationCategory.TITLE, "타이틀 획득!", "처음 반응을 받았어요. 참 잘했어요!"),
    키위새들의_픽(NotificationCategory.TITLE, "타이틀 획득!", "10명의 키위가 좋아해요. 키위새들의 픽!"),
    챌린지_메이커(NotificationCategory.TITLE, "타이틀 획득!", "처음 챌린지를 만드셨네요. 챌린지 메이커!"),
    실패는_성공의_어머니(NotificationCategory.TITLE, "타이틀 획득!", "처음 챌린지를 만드셨네요. 챌린지 메이커!"),
    꺾이지_않는_마음(NotificationCategory.TITLE, "타이틀 획득!", "금새 다음 도전을 하는, 꺾이지 않는 마음!"),
    첫번째_완주(NotificationCategory.TITLE, "타이틀 획득!", "첫 챌린지 성공이네요. 첫번째 완주!"),
    쉬지않고_도전하는(NotificationCategory.TITLE, "타이틀 획득!", "끊임없는 도전! 쉬지않고 도전하는!"),
    혼자보기_아까운(NotificationCategory.TITLE, "타이틀 획득!", "10번이나 공유된, 혼자보기 아까운!"),
    인사이트_수집가(NotificationCategory.TITLE, "타이틀 획득!", "처음 북마크를 저장했네요. 인사이트 수집가!"),
    간직하고_싶은_인사이트(NotificationCategory.TITLE, "타이틀 획득!", "계속계속 보고싶어! 간직하고 싶은 인사이트!"),
    함께하는_즐거움(NotificationCategory.TITLE, "타이틀 획득!", "첫 친구를 초대했네요. 함께하는 즐거움!"),
    마당발(NotificationCategory.TITLE, "타이틀 획득!", "10명이나 초대한 당신은. 마당발!"),
    Shall_We_Keewe(NotificationCategory.TITLE, "타이틀 획득!", "모든 타이틀 획득! 이제...Shall We Keewe?"),

    // reaction
    반응(NotificationCategory.REACTION, "내 인사이트에\n누군가 반응 남김", "%s님이 반응을 남겼어요."),

    // follow
    팔로우(NotificationCategory.FOLLOW, "%s", "%s님이 나를 팔로우 했어요."),

    // comment
    댓글(NotificationCategory.COMMENT, "내 인사이트에 누군가 댓글 남김", "%s님이 댓글을 남겼어요."),
    답글(NotificationCategory.COMMENT, "내 인사이트에 누군가 답글 남김", "%s님이 답글을 남겼어요."),

    // challenge
    챌린지_새로운_인사이트(NotificationCategory.CHALLENGE, "%s", "%s님이 이번주 인사이트를 올렸어요."),
    챌린지_신규_참여(NotificationCategory.CHALLENGE, "%s", "%s님이 %s에 합류했어요!"),
    챌린지_종료_전_실패알림(NotificationCategory.CHALLENGE, "%s", "챌린지가 곧 종료돼요."),
    챌린지_종료(NotificationCategory.CHALLENGE, "%s", "챌린지가 종료되었어요."),
    챌린지_종료_후_재도전(NotificationCategory.CHALLENGE, "%s", "챌린지 재도전하러 가기"),

    // challengeInvite
    챌린지_초대(NotificationCategory.CHALLENGE_INVITE, "%s", "%s에 초대했어요."),
    ;

    NotificationCategory category;
    String title;
    String contents;
}
