package ccc.keewedomain.persistence.domain.common.enums;

import lombok.AllArgsConstructor;

import static ccc.keewedomain.persistence.domain.common.enums.MainActivity.*;

@AllArgsConstructor
public enum Activity {
    MARKETING("마케팅", WORK),
    PLANNING("기획", WORK),
    DESIGN("디자인", WORK),
    DEVELOPMENT("개발", WORK),
    CERTIFICATE("자격증", WORK),
    EMPLOYMENT("취업", WORK),
    STARTUP("창업", WORK),

    REAL_ESTATE("부동산", INVESTMENT),
    VIRTUAL_ASSETS("가상자산", INVESTMENT),
    FINANCIAL_INFORMATION("금융지식", INVESTMENT),
    STOCK("주식", INVESTMENT),
    SIDE_HUSTLE("부업", INVESTMENT),

    ENGLISH("영어", LANGUAGE),
    JAPANESE("일본어", LANGUAGE),
    CHINES("중국어", LANGUAGE),
    OTHER_LANGUAGE("기타 외국어", LANGUAGE),

    EXHIBITION("전시", CULTURE),
    MOVIE("영화", CULTURE),
    PERFORMANCE("공연", CULTURE),
    READING("독서", CULTURE),
    ART("미술", CULTURE),
    PICTURE("사진", CULTURE),
    PUBLISH("출판", CULTURE),
    WRITING("글쓰기", CULTURE),
    ACTING("연기", CULTURE),
    DANCE("댄스", CULTURE),
    MUSICAL_INSTRUMENT("악기 연주", CULTURE),
    VOCAL("보컬", CULTURE),
    PRODUCING("프로듀싱", CULTURE),

    KPOP("KPOP", MUSIC),
    INDIE("인디", MUSIC),
    HIPHOP("힙합", MUSIC),
    POP("해외팝", MUSIC),
    OTHER_MUSIC("기타 음악", MUSIC),

    HIKING("등산", SPORT),
    WORK_OUT("헬스", SPORT),
    YOGA("요가", SPORT),
    PILATES("필라테스", SPORT),
    CLIMBING("클라이밍", SPORT),
    BIKING("바이킹", SPORT),
    SURFING("서핑", SPORT),
    SKI("스키", SPORT),
    BOARD("보드", SPORT),
    BADMINTON("배드민턴", SPORT),
    TENNIS("테니스", SPORT),
    DIET("다이어트", SPORT),

    RESTAURANT("맛집", FOOD),
    COOKING("요리", FOOD),
    BAKING("베이킹", FOOD);

    final String value;
    final MainActivity mainActivity;

    public String getValue() {
        return value;
    }
}

