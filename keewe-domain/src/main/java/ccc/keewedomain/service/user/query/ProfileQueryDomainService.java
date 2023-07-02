package ccc.keewedomain.service.user.query;

import ccc.keewedomain.dto.user.FollowCheckDto;
import ccc.keewedomain.persistence.repository.user.cursor.InviteeSearchCursor;
import ccc.keewedomain.persistence.domain.common.Interest;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.Block;
import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.id.FollowId;
import ccc.keewedomain.persistence.repository.user.BlockQueryRepository;
import ccc.keewedomain.persistence.repository.user.FollowQueryRepository;
import ccc.keewedomain.persistence.repository.user.FollowRepository;
import ccc.keewedomain.persistence.repository.user.TitleAchievedQueryRepository;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileQueryDomainService {
    private final UserDomainService userDomainService;
    private final FollowRepository followRepository;
    private final TitleAchievementRepository titleAchievementRepository;
    private final TitleAchievedQueryRepository titleAchievedQueryRepository;
    private final FollowQueryRepository followQueryRepository;
    private final BlockQueryRepository blockQueryRepository;

    public boolean isFollowing(FollowCheckDto followCheckDto) {
        return followRepository.existsById(FollowId.of(followCheckDto.getUserId(), followCheckDto.getTargetId()));
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
        return followQueryRepository.findFollowersByUserCreatedAtDesc(user, cPage);
    }

    @Transactional(readOnly = true)
    public List<Follow> getFollowees(Long userId, CursorPageable<LocalDateTime> cPage) {
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        return followQueryRepository.findFolloweesByUserCreatedAtDesc(user, cPage);
    }

    @Transactional(readOnly = true)
    public Set<Long> getFollowingTargetIdSet(User user, List<User> targets) {
        return Set.copyOf(followQueryRepository.findFollowingTargetIds(user, targets));
    }

    @Transactional(readOnly = true)
    public List<Block> findBlocksByUserId(Long userId) {
        return blockQueryRepository.findByUserId(userId);
    }

    public Set<Long> findBlockedUserIds(Long userId) {
        return blockQueryRepository.findBlockedUserIdsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<String> getInterests(User user) {
        User userWithInterests = userDomainService.getUserByIdWithInterests(user.getId());

        return userWithInterests.getInterests().stream()
                .map(Interest::getName)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Follow> findRelatedFollows(Long userId, CursorPageable<LocalDateTime> cPage) {
        return followQueryRepository.findAllByUserIdOrderByCreatedAtDesc(userId, cPage);
    }

    @Transactional(readOnly = true)
    public List<Follow> searchRelatedUsers(Long userId, String searchWord, CursorPageable<InviteeSearchCursor> cPage) {
        return followQueryRepository.findByUserIdAndStartsWithNickname(userId, searchWord, cPage);
    }
}
