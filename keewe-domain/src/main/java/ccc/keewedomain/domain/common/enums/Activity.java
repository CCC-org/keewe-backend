package ccc.keewedomain.domain.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Activity {

    KPOP(MainActivity.음악),
    인디(MainActivity.음악),
    힙합(MainActivity.음악),
    해외팝(MainActivity.음악),
    기타_음악(MainActivity.음악),

    축구(MainActivity.운동);

    final MainActivity mainActivity;
}

