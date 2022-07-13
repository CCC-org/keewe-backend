package ccc.keewedomain.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProfileTest {
    @Test
    @DisplayName("링크 패턴 정규식 테스트")
    void test1() {
        Profile profile = Profile.init().build();

        // 정상적인 링크
        profile.createLink("_hello._.world_");

        // 길이 1인 링크
        profile.createLink("_");
        profile.createLink("a");
        profile.createLink("Z");

        // 허용범위 외 특수문자
        assertThrows(IllegalArgumentException.class, () -> profile.createLink("hello?"));
        assertThrows(IllegalArgumentException.class, () -> profile.createLink("hello!"));

        // 시작, 끝이 .
        assertThrows(IllegalArgumentException.class, () -> profile.createLink("hello."));
        assertThrows(IllegalArgumentException.class, () -> profile.createLink(".hello"));
    }

    @Test
    @DisplayName("닉네임 제약조건 테스트")
    void nickname_constraint_test() {
        Profile profile = Profile.init().build();

        // 공백 포매팅
        assertEquals(profile.createNickname("   \n공백  \t    테스트  \n \t \t \b  "), "공백 테스트");

        // whitespace 문자로만 이루어진 경우
        assertThrows(IllegalArgumentException.class, () -> profile.createNickname("\n\n\n\n\n\n\n\n\n"));

        // 닉네임의 길이가 초과된 경우(11자)
        assertThrows(IllegalArgumentException.class, () -> profile.createNickname("01234567891"));

        // 닉네임의 길이가 제한과 일치하는 경우
        profile.createNickname("0123456789");

        // 여러 유형의 문자가 섞여있는 경우
        profile.createNickname("   \uD83C\uDDF0\uD83C\uDDF7\uD83D\uDE011한    \t \b\b\b\b\b \n      글B❣️✅#️⃣ ");
    }
}