package ccc.keewedomain.domain.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Activity {

    KPOP(MainActivity.음악),
    축구(MainActivity.운동);

    MainActivity mainActivity;
}

