package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.*;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.common.Link;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.ProfileLink;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.repository.user.ProfileRepository;
import ccc.keewedomain.service.ProfileDomainService;
import ccc.keewedomain.service.ProfileLinkDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileLinkDomainService profileLinkDomainService;
    private final ProfileDomainService profileDomainService;

    @Transactional
    public LinkCreateResponseDto createLink(LinkCreateRequestDto createLinkDto, Long userId) {
        String link = createLinkDto.getLink();
        Profile profile = profileRepository.findByIdAndUserIdAndDeletedFalseOrElseThrow(createLinkDto.getProfileId(), userId);

        checkDuplicateLinkOrElseThrows(link);

        profile.createLink(link);

        return LinkCreateResponseDto.builder()
                .link(link)
                .status(profile.getProfileStatus())
                .build();
    }

    @Transactional
    public NicknameCreateResponseDto createNickname(NicknameCreateRequestDto nicknameCreateDto, Long userId) {
        String nickname = nicknameCreateDto.getNickname();

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

    @Transactional
    public void createProfileLinks(ProfileLinkCreateRequestDto requestDto) {
        User user = SecurityUtil.getUser();
        Profile profile = profileDomainService.getByIdAndUserIdOrElseThrow(requestDto.getProfileId(), user.getId());
        List<ProfileLink> profileLinks = requestDto.getLinks().stream()
                .map(linkDto -> Link.of(linkDto.getUrl(), linkDto.getType()))
                .map(link -> profileLinkDomainService.save(profile, link))
                .collect(Collectors.toList());

        profile.createProfileLinks(profileLinks);
    }


    private void checkDuplicateLinkOrElseThrows(String link) {
        if (profileRepository.existsByLinkAndDeletedFalse(link))
            throw new IllegalArgumentException("이미 존재하는 링크입니다.");
    }
}