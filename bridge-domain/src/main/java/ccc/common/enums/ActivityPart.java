package ccc.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ActivityPart {

    KPOP(MainActivity.음악),
    축구(MainActivity.운동);

    MainActivity mainActivity;
}

