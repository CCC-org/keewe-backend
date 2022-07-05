package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.CreateLinkDto;

import ccc.keeweapi.dto.user.NicknameCreateDto;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.repository.user.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

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
    public void createNickname(NicknameCreateDto nicknameCreateDto) {
        String nickname = nicknameCreateDto.getNickname();

        checkNicknameLength(nickname);

        Profile profile = profileRepository.findByIdAndUserIdAndDeletedFalseOrElseThrow(nicknameCreateDto.getProfileId(), 1L);
        profile.createNickname(nickname);
    }

    private void checkNicknameLength(String nickname) {
        if (nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new IllegalArgumentException("김영기 바보!!");
        }
    }

    private void checkDuplicateLinkOrElseThrows(String link) {
        if (profileRepository.existsByLinkAndDeletedFalse(link))
            throw new IllegalArgumentException("허보성 바보!!");
    }
}