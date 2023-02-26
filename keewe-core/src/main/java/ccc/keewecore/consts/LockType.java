package ccc.keewecore.consts;

import lombok.Getter;

@Getter
public enum LockType {
    CHALLENGE_PARTICIPATE("CP:%s"),
    ;

    private String keyFormat;

    LockType(String keyFormat) {
        this.keyFormat = keyFormat;
    }
}