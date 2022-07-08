package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.LinkCreateRequestDto;

import ccc.keeweapi.dto.user.LinkCreateResponseDto;
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
    public LinkCreateResponseDto createLink(LinkCreateRequestDto createLinkDto, Long userId) {
        String link = createLinkDto.getLink();
        Profile profile = profileRepository.findByIdAndUserIdAndDeletedFalseOrElseThrow(createLinkDto.getProfileId(), userId);

        checkDuplicateLinkOrElseThrows(link);

        profile.createLink(link);

        return new LinkCreateResponseDto(link, profile.getProfileStatus());
    }

    @Transactional
    public NicknameCreateResponseDto createNickname(NicknameCreateRequestDto nicknameCreateDto, Long userId) {
        String nickname = nicknameCreateDto.getNickname();

        checkNicknameLength(nickname);

        Profile profile = profileRepository.findByIdAndUserIdAndDeletedFalseOrElseThrow(
                nicknameCreateDto.getProfileId(),
                userId);
        profile.createNickname(nickname);

        return new NicknameCreateResponseDto(nickname, profile.getProfileStatus());
    }

    private void checkNicknameLength(String nickname) {
        if (getGraphemeLength(nickname) > NICKNAME_MAX_LENGTH) {
            throw new IllegalArgumentException("김영기 바보!!");
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