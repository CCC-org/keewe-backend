package ccc.keewedomain.service;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewecore.utils.StringLengthUtil;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.ProfileLink;
import ccc.keewedomain.repository.user.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileDomainService {
    private final ProfileRepository profileRepository;

    public Long save(Profile profile) {
        return profileRepository.save(profile).getId();
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

    public void initProfileLinks(Long id, List<ProfileLink> profileLinks) {
        Profile profile = getByIdOrElseThrow(id);
        profile.initProfileLinks(profileLinks);
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
