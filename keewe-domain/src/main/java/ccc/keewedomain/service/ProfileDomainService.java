package ccc.keewedomain.service;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewecore.utils.StringLengthUtil;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.SocialLink;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.repository.user.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

import static ccc.keewedomain.domain.user.enums.ProfileStatus.ACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileDomainService {
    private final ProfileRepository profileRepository;
    private final int SOCIAL_LINKS_SIZE = 5;

    public Long save(Profile profile) {
        return profileRepository.save(profile).getId();
    }

    public Profile createProfile(User user) {
        return profileRepository.save(Profile.of(user));
    }

    public Profile getByIdOrElseThrow(Long id) {
        return profileRepository.findById(id).orElseThrow(() ->
                new KeeweException(KeeweRtnConsts.ERR422)
        );
    }

    public Profile getAndVerifyOwnerOrElseThrow(Long id, Long userId) {
        return profileRepository.findByIdAndUserIdAndDeletedFalse(id, userId).orElseThrow(() ->
                new KeeweException(KeeweRtnConsts.ERR422)
        );
    }

    public Profile createNickname(Long id, String nickname) {
        nickname = applyNicknameFormat(nickname);
        verifyNicknameOrElseThrow(nickname);
        Profile profile = getByIdOrElseThrow(id);
        profile.createNickname(nickname);
        return profile;
    }

    public void initSocialLinks(Long id, List<SocialLink> socialLinks) {
        verifySocialLinkSize(socialLinks);
        Profile profile = getByIdOrElseThrow(id);
        profile.initSocialLinks(socialLinks);
    }

    public List<Profile> getIncompleteProfiles(Long userId) {
        return profileRepository.findByUserIdAndProfileStatusNotAndDeletedFalse(userId, ACTIVE);
    }

    private void verifySocialLinkSize(List<SocialLink> socialLinks) {
        if (socialLinks.size() > Profile.SOCIAL_LINKS_MAX_SIZE) {
            throw new KeeweException(KeeweRtnConsts.ERR426);
        }
    }

    private String applyNicknameFormat(String nickname) {
        return nickname
                .trim()
                .replaceAll("\b", "")
                .replaceAll("\\s+", " ");
    }

    private void verifyNicknameOrElseThrow(String nickname) {
        long length = StringLengthUtil.getGraphemeLength(nickname);

        if (length > Profile.NICKNAME_MAX_LENGTH) {
            throw new KeeweException(KeeweRtnConsts.ERR420);
        }

        if (length == 0) {
            throw new KeeweException(KeeweRtnConsts.ERR421);
        }
    }
}
