package ccc.keewedomain.service.user;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.user.*;
import ccc.keewedomain.persistence.domain.title.Title;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.title.id.TitleAchievementId;
import ccc.keewedomain.persistence.domain.user.Block;
import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.ProfilePhoto;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.id.BlockId;
import ccc.keewedomain.persistence.domain.user.id.FollowId;
import ccc.keewedomain.persistence.repository.user.*;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keeweinfra.service.image.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private final BlockRepository blockRepository;
    private final BlockQueryRepository blockQueryRepository;

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

        return getFollowingTargetIdSet(FollowCheckDto.of(user.getId(), target.getId()));
    }

    public boolean getFollowingTargetIdSet(FollowCheckDto followCheckDto) {
        return followRepository.existsById(FollowId.of(followCheckDto.getUserId(), followCheckDto.getTargetId()));
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
    public Set<Long> getFollowingTargetIdSet(User user, List<User> targets) {
        return Set.copyOf(followQueryRepository.findFollowingTargetIds(user, targets));
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

    private void removeRelation(User user, User blockedUser) {
        followRepository.deleteById(FollowId.of(user.getId(), blockedUser.getId()));
        followRepository.deleteById(FollowId.of(blockedUser.getId(), user.getId()));
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

    @Transactional(readOnly = true)
    public List<Block> findBlocksByUserId(Long userId) {
        return blockQueryRepository.findByUserId(userId);
    }

    @Transactional
    public User updateProfile(Long userId, ProfileUpdateDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        Title title = dto.getRepTitleId() == null
                ? null
                : getTitleAchievementById(userId, dto.getRepTitleId()).getTitle();

        if(dto.getProfileImage() != null) {
            deleteProfilePhoto(user);
            uploadProfilePhoto(UploadProfilePhotoDto.of(userId, dto.getProfileImage()));
        } else if(dto.isDeletePhoto()) {
            deleteProfilePhoto(user);
        }

        user.updateProfile(dto.getNickname(), dto.getInterests(), title, dto.getIntroduction());

        return user;
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

    private TitleAchievement getTitleAchievementById(Long userId, Long titleId) {
        return titleAchievementRepository.findById(TitleAchievementId.of(userId, titleId))
                .orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR480));
    }

    private void deleteProfilePhoto(User user) {
        if(user.getProfilePhoto() != null ) {
            storeService.delete(user.getProfilePhotoURL());
            user.deleteProfilePhoto();
        }
    }
}
