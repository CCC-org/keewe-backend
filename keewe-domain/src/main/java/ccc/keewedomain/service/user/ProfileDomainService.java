package ccc.keewedomain.service.user;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.user.FollowCheckDto;
import ccc.keewedomain.dto.user.FollowToggleDto;
import ccc.keewedomain.dto.user.UploadProfilePhotoDto;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.*;
import ccc.keewedomain.dto.user.OnboardDto;
import ccc.keewedomain.persistence.domain.user.id.FollowId;
import ccc.keewedomain.persistence.repository.user.*;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keeweinfra.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileDomainService {
    private final UserDomainService userDomainService;
    private final FollowRepository followRepository;
    private final StoreService storeService;
    private final TitleAchievementRepository titleAchievementRepository;
    private final TitleAchievedQueryRepository titleAchievedQueryRepository;
    private final FollowQueryRepository followQueryRepository;

    private final UserQueryRepository userQueryRepository;

    public User onboard(OnboardDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        user.initProfile(dto.getNickname(), dto.getInterests());
        return user;
    }

    public boolean toggleFollowership(FollowToggleDto followDto) {
        validateSelfFollowing(followDto);

        User user = userDomainService.getUserByIdOrElseThrow(followDto.getUserId());
        User target = userDomainService.getUserByIdOrElseThrow(followDto.getTargetId());

        FollowId followId = FollowId.of(user.getId(), target.getId());
        followRepository.findById(followId)
                        .ifPresentOrElse(
                                follow -> {
                                    log.info("[PDS::toggleFollowership] Found Relation followee {}, follower {}", user.getId(), target.getId());
                                    follow.removeRelation(user, target);
                                    followRepository.delete(follow);
                                },
                                () -> {
                                    log.info("[PDS::toggleFollowership] Not Found Relation followee {}, follower {}", user.getId(), target.getId());
                                    Follow relation = Follow.makeRelation(user, target);
                                    followRepository.save(relation);
                                }
                        );

        return isFollowing(FollowCheckDto.of(user.getId(), target.getId()));
    }

    public boolean isFollowing(FollowCheckDto followCheckDto) {
        return followRepository.existsById(FollowId.of(followCheckDto.getTargetId(), followCheckDto.getUserId()));
    }

    public String uploadProfilePhoto(UploadProfilePhotoDto uploadProfilePhotoDto) {
        User user = userDomainService.getUserByIdOrElseThrow(uploadProfilePhotoDto.getUserId());
        String imageURL = storeService.upload(uploadProfilePhotoDto.getImageFile());

        user.setProfilePhoto(ProfilePhoto.of(imageURL)); // TODO : image remove by store service

        return imageURL;
    }

    private void validateSelfFollowing(FollowToggleDto followDto) {
        if(followDto.getTargetId() == followDto.getUserId()) {
            throw new KeeweException(KeeweRtnConsts.ERR446);
        }
    }

    public Long getFollowerCount(User user) {
        return followRepository.countByFollowee(user);
    }

    public Long getFollowingCount(User user) {
        return followRepository.countByFollower(user);
    }

    @Transactional(readOnly = true)
    public List<TitleAchievement> getTitleAchievements(Long userId, int n) {
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        return titleAchievedQueryRepository.findByUserIdOrderByCreatedAtDesc(user, n);
    }

    @Transactional(readOnly = true)
    public Long getAchievedTitleCount(Long userId) {
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        return titleAchievementRepository.countByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Follow> getFollowers(Long userId, CursorPageable<LocalDateTime> cPage) {
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        return userQueryRepository.findFollowersByUserCreatedAtDesc(user, cPage);
    }

    @Transactional(readOnly = true)
    public List<Follow> getFollowees(Long userId, CursorPageable<LocalDateTime> cPage) {
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        return userQueryRepository.findFolloweesByUserCreatedAtDesc(user, cPage);
    }

    @Transactional(readOnly = true)
    public Set<Long> isFollowing(User user, List<User> targets) {
        return followQueryRepository.existsByIds(user, targets);
    }
}
