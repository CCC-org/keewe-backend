package ccc.keewedomain.service.user.command;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.user.FollowCheckDto;
import ccc.keewedomain.dto.user.FollowFromInsightDto;
import ccc.keewedomain.dto.user.FollowToggleDto;
import ccc.keewedomain.dto.user.OnboardDto;
import ccc.keewedomain.dto.user.ProfileUpdateDto;
import ccc.keewedomain.dto.user.UploadProfilePhotoDto;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.title.Title;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.title.id.TitleAchievementId;
import ccc.keewedomain.persistence.domain.user.Block;
import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.FollowFromInsight;
import ccc.keewedomain.persistence.domain.user.ProfilePhoto;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.id.BlockId;
import ccc.keewedomain.persistence.domain.user.id.FollowFromInsightId;
import ccc.keewedomain.persistence.domain.user.id.FollowId;
import ccc.keewedomain.persistence.repository.user.BlockRepository;
import ccc.keewedomain.persistence.repository.user.FollowFromInsightRepository;
import ccc.keewedomain.persistence.repository.user.FollowRepository;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import ccc.keeweinfra.service.image.StoreService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileCommandDomainService {
    private final UserDomainService userDomainService;
    private final FollowRepository followRepository;
    private final StoreService storeService;
    private final TitleAchievementRepository titleAchievementRepository;
    private final BlockRepository blockRepository;
    private final NotificationCommandDomainService notificationCommandDomainService;
    private final ProfileQueryDomainService profileQueryDomainService;
    private final MQPublishService mqPublishService;
    private final FollowFromInsightRepository followFromInsightRepository;
    private final InsightQueryDomainService insightQueryDomainService;

    public User onboard(OnboardDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        user.initProfile(dto.getNickname(), dto.getInterests());
        return user;
    }

    public boolean toggleFollowership(FollowToggleDto followDto, Long insightId) {
        followDto.validateSelfFollowing(followDto);

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
                            afterFollowing(followRepository.save(relation), insightId);
                        }
                );
        return profileQueryDomainService.isFollowing(FollowCheckDto.of(user.getId(), target.getId()));
    }

    public String uploadProfilePhoto(UploadProfilePhotoDto uploadProfilePhotoDto) {
        User user = userDomainService.getUserByIdOrElseThrow(uploadProfilePhotoDto.getUserId());
        String imageURL = storeService.upload(
                uploadProfilePhotoDto.getImageFile(),
                KeeweConsts.PROFILE_PHOTO_WIDTH,
                KeeweConsts.PROFILE_PHOTO_HEIGHT
        );

        user.setProfilePhoto(ProfilePhoto.of(imageURL)); // TODO : image remove by store service

        return imageURL;
    }

    @Transactional
    public User updateProfile(Long userId, ProfileUpdateDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        Title title = dto.getRepTitleId() == null
                ? null
                : getTitleAchievementById(userId, dto.getRepTitleId()).getTitle();

        if(dto.isUpdatePhoto()) {
            updateProfilePhoto(user, dto.getProfileImage());
        }

        user.updateProfile(dto.getNickname(), dto.getInterests(), title, dto.getIntroduction());

        return user;
    }

    @Transactional
    public Long blockUser(Long userId, Long blockUserId) {
        validateBlockUser(userId, blockUserId);

        User user = userDomainService.getUserByIdOrElseThrow(userId);
        User blockedUser = userDomainService.getUserByIdOrElseThrow(blockUserId);

        removeRelation(user, blockedUser);

        log.info("[PDS::blockUser] user {}, blockedUser {}", user.getId(), blockedUser.getId());
        Block block = blockRepository.save(Block.of(user, blockedUser));
        return block.getBlockedUser().getId();
    }

    @Transactional
    public Long unblockUser(Long userId, Long blockedUserId) {
        blockRepository.findById(BlockId.of(userId, blockedUserId))
                .ifPresentOrElse(
                        blockRepository::delete,
                        () -> { throw new KeeweException(KeeweRtnConsts.ERR452); }
                );
        log.info("[PDS::unblockUser] user {}, blockedUser {}", userId, blockedUserId);
        return blockedUserId;
    }

    @Transactional
    public FollowFromInsight addFollowFromInsight(FollowFromInsightDto dto) {
        insightQueryDomainService.validateWriter(dto.getFolloweeId(), dto.getInsightId());
        FollowFromInsightId id = FollowFromInsightId.of(dto.getFollowerId(), dto.getFolloweeId(), dto.getInsightId());
        if(followFromInsightRepository.existsById(id)) {
            log.info("[PCDS::addFollowFromInsight] Follow history already exists - followerId({}), followeeId({}), insightId({})",
                    dto.getFollowerId(), dto.getFolloweeId(), dto.getInsightId());
            return null;
        }

        FollowFromInsight followFromInsight = FollowFromInsight.of(
                userDomainService.getUserByIdOrElseThrow(dto.getFollowerId()),
                userDomainService.getUserByIdOrElseThrow(dto.getFolloweeId()),
                insightQueryDomainService.getByIdOrElseThrow(dto.getInsightId())
        );
        return followFromInsightRepository.save(followFromInsight);
    }

    private void removeRelation(User user, User blockedUser) {
        followRepository.deleteByIdIfExists(FollowId.of(user.getId(), blockedUser.getId()));
        followRepository.deleteByIdIfExists(FollowId.of(blockedUser.getId(), user.getId()));
    }

    private void validateBlockUser(Long userId, Long blockUserId) {
        if(userId.equals(blockUserId)) {
            throw new KeeweException(KeeweRtnConsts.ERR451);
        }

        BlockId blockId = BlockId.of(userId, blockUserId);
        if(blockRepository.existsById(blockId)) {
            throw new KeeweException(KeeweRtnConsts.ERR450);
        }
    }

    private void updateProfilePhoto(User user, MultipartFile profilePhoto) {
        deleteProfilePhoto(user);
        if(profilePhoto != null) {
            uploadProfilePhoto(UploadProfilePhotoDto.of(user.getId(), profilePhoto));
        }
    }

    private void deleteProfilePhoto(User user) {
        if(user.getProfilePhoto() != null ) {
            storeService.delete(user.getProfilePhotoURL());
            user.deleteProfilePhoto();
        }
    }

    private void afterFollowing(Follow follow, Long insightId) {
        try {
            Notification notification = Notification.of(
                    follow.getFollowee(),
                    NotificationContents.팔로우,
                    String.valueOf(follow.getFollower().getId()) // note. 클릭 시 팔로우 한 사람의 프로필로 이동 대비
            );
            notificationCommandDomainService.save(notification);
            if(insightId != null) {
                log.info("[PDS::afterFollowing] ");
                publishFollowFromInsightEvent(FollowFromInsightDto.of(
                        follow.getFollower().getId(),
                        follow.getFollowee().getId(),
                        insightId));
            }

        } catch (Throwable t) {
            log.warn("[PDS::afterFollowing] 팔로우 후 작업 실패 - 팔로우하는사람({}), 팔로우당하는사람({})", follow.getFollower().getId(), follow.getFollowee().getId(), t);
        }
    }

    private void publishFollowFromInsightEvent(FollowFromInsightDto dto) {
        mqPublishService.publish(KeeweConsts.FOLLOW_FROM_INSIGHT_EXCHANGE, dto);
    }

    private TitleAchievement getTitleAchievementById(Long userId, Long titleId) {
        return titleAchievementRepository.findById(TitleAchievementId.of(userId, titleId))
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR480));
    }
}
