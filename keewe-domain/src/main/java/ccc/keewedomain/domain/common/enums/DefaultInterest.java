package ccc.keewedomain.domain.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DefaultInterest {
    마케팅,
    기획,
    디자인,
    개발,
    창업,
    가상자산,
    주식,
    전시,
    영화,
    공연,
    독서,
    미술,
    사진,
    글쓰기,
    맛집,
    요리,
    뷰티,
    패션,
    여행,
    인테리어,
    일상,
    육아,
    반려동물,
    음악;

    @Getter
    private static final List<String> names = Arrays.stream(DefaultInterest.values())
            .map(DefaultInterest::name)
            .collect(Collectors.toUnmodifiableList());
}
