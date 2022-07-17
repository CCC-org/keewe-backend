package ccc.keeweapi.service.user;

import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.common.Link;
import ccc.keewedomain.domain.common.enums.Activity;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.SocialLink;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.repository.user.ProfileRepository;
import ccc.keewedomain.service.ProfileDomainService;
import ccc.keewedomain.service.SocialLinkDomainService;
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
    private final SocialLinkDomainService socialLinkDomainService;
    private final ProfileDomainService profileDomainService;

    @Transactional
    public LinkCreateResponse createLink(LinkCreateRequest createLinkDto, Long userId) {
        String link = createLinkDto.getLink();
        Profile profile = profileRepository.findByIdAndUserIdAndDeletedFalseOrElseThrow(createLinkDto.getProfileId(), userId);

        checkDuplicateLinkOrElseThrows(link);

        profile.createLink(link);

        return LinkCreateResponse.builder()
                .link(link)
                .status(profile.getProfileStatus())
                .build();
    }

    @Transactional
    public NicknameCreateResponse createNickname(NicknameCreateRequest request) {
        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(request.getProfileId(), SecurityUtil.getUserId());
        profileDomainService.createNickname(profile.getId(), request.getNickname());
        return NicknameCreateResponse.of(profile.getNickname(), profile.getProfileStatus());
    }

    // TODO : 현재는 단순 텍스트 비교로 검색(contains). 나중에 NLP...?
    public ActivitiesSearchResponse searchActivities(String keyword) {
        List<Activity> result = Arrays.stream(Activity.values())
                .filter(activity -> {
                    String value = activity.toString().replace("_", " ");
                    return value.contains(keyword) || keyword.contains(value);
                })
                .collect(Collectors.toList());

        return new ActivitiesSearchResponse(result);
    }

    @Transactional
    public void createSocialLinks(SocialLinkCreateRequest request) {
        List<Link> links = request.getLinks().stream()
                .map(linkDto -> Link.of(linkDto.getUrl(), linkDto.getType()))
                .collect(Collectors.toList());

        Profile profile = profileDomainService.getAndVerifyOwnerOrElseThrow(request.getProfileId(), SecurityUtil.getUserId());
        List<SocialLink> socialLinks = socialLinkDomainService.saveAll(profile, links);
        profileDomainService.initSocialLinks(profile.getId(), socialLinks);
    }


    @Transactional(readOnly = true)
    public IncompleteProfileResponse getIncompleteProfile() {
        return profileDomainService.getIncompleteProfiles(SecurityUtil.getUserId()).stream()
                .map(IncompleteProfileResponse::getExistResult)
                .findFirst()
                .orElse(IncompleteProfileResponse.getNotExistResult());
    }


    private void checkDuplicateLinkOrElseThrows(String link) {
        if (profileRepository.existsByLinkAndDeletedFalse(link))
            throw new IllegalArgumentException("이미 존재하는 링크입니다.");
    }
}