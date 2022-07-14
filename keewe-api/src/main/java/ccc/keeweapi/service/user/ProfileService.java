package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.*;

import ccc.keeweapi.dto.user.*;
import ccc.keewedomain.domain.common.enums.Activity;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.repository.user.ProfileRepository;
import ccc.keewedomain.service.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
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
    public NicknameCreateResponseDto createNickname(Long profileId, Long userId, String nickname) {
        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(profileId, userId);
        profileDomainService.createNickname(profile.getId(), nickname);
        return NicknameCreateResponseDto.of(profile.getNickname(), profile.getProfileStatus());
    }

    // TODO : 현재는 단순 텍스트 비교로 검색(contains). 나중에 NLP...?
    public ActivitiesSearchResponseDto searchActivities(String keyword) {
        List<Activity> result = Arrays.stream(Activity.values())
                .filter(activity -> {
                    String value = activity.toString().replace("_", " ");
                    return value.contains(keyword) || keyword.contains(value);
                })
                .collect(Collectors.toList());

        return new ActivitiesSearchResponseDto(result);
    }


    private void checkDuplicateLinkOrElseThrows(String link) {
        if (profileRepository.existsByLinkAndDeletedFalse(link))
            throw new IllegalArgumentException("이미 존재하는 링크입니다.");
    }
}