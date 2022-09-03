package ccc.keewedomain.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProfileTest {
    @Test
    @DisplayName("링크 패턴 정규식 테스트")
    void test1() {
        Profile profile = new Profile();
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
}