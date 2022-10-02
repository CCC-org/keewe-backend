package ccc.keewedomain.persistence.domain.common.enums;

import lombok.val;

public enum MainActivity {
    WORK("실무/취업"),
    INVESTMENT("재태크"),
    LANGUAGE("언어/외국어"),
    CULTURE("문화/예술"),
    MUSIC("음악"),
    SPORT("운동/액티비티"),
    FOOD("음식");

    final String value;

    MainActivity(String value) {
        this.value = value;
    }
}
