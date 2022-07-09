package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.CreateLinkDto;

import ccc.keeweapi.dto.user.NicknameCreateRequestDto;
import ccc.keeweapi.dto.user.NicknameCreateResponseDto;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.repository.user.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.BreakIterator;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    private static final int NICKNAME_MAX_LENGTH = 12;

    @Transactional
    public Long createLink(CreateLinkDto createLinkDto) {
        String link = createLinkDto.getLink();
        Profile profile = profileRepository.findByIdAndUserIdAndDeletedFalseOrElseThrow(createLinkDto.getProfileId(), 5L);

        checkDuplicateLinkOrElseThrows(link);

        profile.createLink(link);

        return 0L;
    }

    @Transactional
    public NicknameCreateResponseDto createNickname(NicknameCreateRequestDto nicknameCreateDto, Long userId) {
        String nickname = nicknameCreateDto.getNickname();

        checkNicknameLength(nickname);

        Profile profile = profileRepository.findByIdAndUserIdAndDeletedFalseOrElseThrow(
                nicknameCreateDto.getProfileId(),
                userId
        );

        profile.createNickname(nickname);

        return NicknameCreateResponseDto.builder()
                .nickname(nickname)
                .status(profile.getProfileStatus())
                .build();
    }

    private void checkNicknameLength(String nickname) {
        if (getGraphemeLength(nickname) > NICKNAME_MAX_LENGTH) {
            throw new IllegalArgumentException("닉네임의 길이가 12자를 초과해요.");
        }
    }

    private long getGraphemeLength(String s) {
        long length = getCharacterBoundaryCount(s) - get4ByteEmojiCount(s);
        return length;
    }

    private long get4ByteEmojiCount(String s) {
        return s.chars()
                .filter(i -> Character.isSurrogate((char) i))
                .count() / 4;
    }

    private long getCharacterBoundaryCount(String s) {
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(s);
        long count = 0;
        while (it.next() != BreakIterator.DONE) {
            count++;
        }

        return count;
    }

    private void checkDuplicateLinkOrElseThrows(String link) {
        if (profileRepository.existsByLinkAndDeletedFalse(link))
            throw new IllegalArgumentException("허보성 바보!!");
    }
}