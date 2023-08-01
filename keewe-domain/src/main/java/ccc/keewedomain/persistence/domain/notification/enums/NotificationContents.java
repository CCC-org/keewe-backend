package ccc.keewedomain.persistence.domain.notification.enums;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum NotificationContents {
    // title
    시작이반(NotificationCategory.TITLE, "타이틀 획득!", "키위에 어서오세요. 시작이 반!", 1000L),
    위대한_첫_도약(NotificationCategory.TITLE, "타이틀 획득!", "첫 인사이트를 올리셨네요. 위대한 첫 도약!", 2000L),
    초보_기록가(NotificationCategory.TITLE, "타이틀 획득!", "꾸준함이 중요하죠. 초보 기록가!", 2001L),
    중급_기록가(NotificationCategory.TITLE, "타이틀 획득!", "꾸준함으로 만든 10개의 글. 중급 기록가!", 2002L),
    고급_기록가(NotificationCategory.TITLE, "타이틀 획득!", "언제 50개나 쓰셨죠?! 고급 기록가!", 2003L),
    인사이트의_신(NotificationCategory.TITLE, "타이틀 획득!", "당신은 그저 God...인사이트의 신!", 2004L),
    혼자서도_잘_해요(NotificationCategory.TITLE, "타이틀 획득!", "알아서 척척 하는 당신은, 혼자서도 잘 해요!", 2005L),
    두근두근_첫만남(NotificationCategory.TITLE, "타이틀 획득!", "첫 팔로워라니...두근두근 첫만남!", 3000L),
    자타공인_인기인(NotificationCategory.TITLE, "타이틀 획득!", "너도나도 닮고싶은 당신. 자타공인 인기인!", 3001L),
    피리부는_사나이(NotificationCategory.TITLE, "타이틀 획득!", "100명이 쫓아다니는 인기인. 피리부는 사나이!", 3002L),
    정이_많은(NotificationCategory.TITLE, "타이틀 획득!", "40명을 마음에 담았네요. 정이 많은!", 3003L),
    참_잘했어요(NotificationCategory.TITLE, "타이틀 획득!", "처음 반응을 받았어요. 참 잘했어요!", 4000L),
    아낌없이_주는_나무(NotificationCategory.TITLE, "타이틀 획득!", "50번의 마음을 전했어요. 아낌없이 주는 나무!", 4001L),
    키위새들의_픽(NotificationCategory.TITLE, "타이틀 획득!", "10명의 키위가 좋아해요. 키위새들의 픽!", 4002L),
    챌린지_메이커(NotificationCategory.TITLE, "타이틀 획득!", "처음 챌린지를 만드셨네요. 챌린지 메이커!", 5000L),
    실패는_성공의_어머니(NotificationCategory.TITLE, "타이틀 획득!", "처음 챌린지를 만드셨네요. 챌린지 메이커!", 5001L),
    꺾이지_않는_마음(NotificationCategory.TITLE, "타이틀 획득!", "금새 다음 도전을 하는, 꺾이지 않는 마음!", 5004L),
    첫번째_완주(NotificationCategory.TITLE, "타이틀 획득!", "첫 챌린지 성공이네요. 첫번째 완주!", 5002L),
    쉬지않고_도전하는(NotificationCategory.TITLE, "타이틀 획득!", "끊임없는 도전! 쉬지않고 도전하는!", 5003L),
    혼자보기_아까운(NotificationCategory.TITLE, "타이틀 획득!", "10번이나 공유된, 혼자보기 아까운!", 6000L),
    인사이트_수집가(NotificationCategory.TITLE, "타이틀 획득!", "처음 북마크를 저장했네요. 인사이트 수집가!", 7000L),
    간직하고_싶은_인사이트(NotificationCategory.TITLE, "타이틀 획득!", "계속계속 보고싶어! 간직하고 싶은 인사이트!", 7001L),
    함께하는_즐거움(NotificationCategory.TITLE, "타이틀 획득!", "첫 친구를 초대했네요. 함께하는 즐거움!", 8000L),
    마당발(NotificationCategory.TITLE, "타이틀 획득!", "10명이나 초대한 당신은. 마당발!", 8001L),
    Shall_We_Keewe(NotificationCategory.TITLE, "타이틀 획득!", "모든 타이틀 획득! 이제...Shall We Keewe?", 9000L),

    // reaction
    반응(NotificationCategory.REACTION, "내 인사이트에\n누군가 반응 남김", "%s님이 반응을 남겼어요.", -1L),

    // follow
    팔로우(NotificationCategory.FOLLOW, "%s", "%s님이 나를 팔로우 했어요.", -1L),

    // comment
    댓글(NotificationCategory.COMMENT, "내 인사이트에 누군가 댓글 남김", "%s님이 댓글을 남겼어요.", -1L),
    답글(NotificationCategory.COMMENT, "내 인사이트에 누군가 답글 남김", "%s님이 답글을 남겼어요.", -1L),

    // challenge
    챌린지_새로운_인사이트(NotificationCategory.CHALLENGE, "%s", "%s님이 이번주 인사이트를 올렸어요.", -1L),
    챌린지_신규_참여(NotificationCategory.CHALLENGE, "%s", "%s님이 %s에 합류했어요!", -1L),
    챌린지_성공(NotificationCategory.CHALLENGE, "%s", "챌린지를 성공적으로 완주했어요!\n더 많은 챌린지를 둘러볼까요?", -1L),
    챌린지_실패(NotificationCategory.CHALLENGE, "%s", "챌린지가 종료됐어요!\n%s 챌린지에 재도전할까요?", -1L),
    챌린지_채찍(NotificationCategory.CHALLENGE, "%s", "오늘 감명받은 링크가 있나요?\n챌린지를 완주해보세요.", -1L),

    // challengeInvite
    챌린지_초대(NotificationCategory.CHALLENGE_INVITE, "%s", "%s에 초대했어요.", -1L),
    ;

    final NotificationCategory category;
    final String title;
    final String contents;
    final Long extraId;

    public static NotificationContents findByTitleId(Long titleId) {
        return Arrays.stream(NotificationContents.values())
                .filter(notificationContents -> notificationContents.getExtraId().equals(titleId))
                .filter(notificationContents -> notificationContents.getCategory().equals(NotificationCategory.TITLE))
                .findFirst()
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR999));
    }
}
