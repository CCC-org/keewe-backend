package ccc.keewedomain.domain.user;

import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.repository.user.ProfileRepository;
import ccc.keewedomain.service.user.ProfileDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileDomainServiceTest {

    @InjectMocks
    ProfileDomainService profileDomainService;

    @Mock
    ProfileRepository profileRepository;

    @Test
    @DisplayName("닉네임 생성 제약조건 테스트")
    void nickname_constraint_test() {
        Profile profile = new Profile();

        when(profileRepository.findById(any()))
                .thenReturn(Optional.of(profile));

        // 공백 포매팅
        String nickname = profileDomainService
                .createNickname(0L, "   \n공백  \t    테스트  \n \t \t \b  ")
                .getNickname();
        assertEquals(nickname, "공백 테스트");

        // whitespace 문자로만 이루어진 경우
        assertThrows(KeeweException.class, () -> profileDomainService.createNickname(0L, "\n\n\n\n\n\n\n\n\n"));

        // 닉네임의 길이가 초과된 경우(11자)
        assertThrows(KeeweException.class,
                () -> profileDomainService.createNickname(0L, "01234567891"));

        // 닉네임의 길이가 제한과 일치하는 경우
        assertDoesNotThrow(() -> profileDomainService.createNickname(0L, "0123456789"));

        // 여러 유형의 문자가 섞여있는 경우
        assertDoesNotThrow(() ->
                profile.createNickname("   \uD83C\uDDF0\uD83C\uDDF7\uD83D\uDE011한    \t \b\b\b\b\b \n      글B❣️✅#️⃣ "));
    }
}
