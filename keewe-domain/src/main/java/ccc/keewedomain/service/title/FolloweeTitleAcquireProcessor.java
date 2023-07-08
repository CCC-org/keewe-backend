package ccc.keewedomain.service.title;

import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.persistence.domain.title.enums.FollowingTitle;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.user.FollowQueryRepository;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

@Service
@Slf4j
// note. 나를 팔로우한 사람 수 기반 타이틀 처리
public class FolloweeTitleAcquireProcessor extends AbstractTitleAcquireProcessor {
    private final UserDomainService userDomainService;
    private final FollowQueryRepository followQueryRepository;

    protected FolloweeTitleAcquireProcessor(
            MQPublishService mqPublishService,
            TitleAchievementRepository titleAchievementRepository,
            UserDomainService userDomainService,
            UserRepository userRepository,
            TitleRepository titleRepository,
            FollowQueryRepository followQueryRepository
    ) {
        super(mqPublishService, titleAchievementRepository, userDomainService, userRepository, titleRepository);
        this.userDomainService = userDomainService;
        this.followQueryRepository = followQueryRepository;
    }

    @Override
    public TitleCategory getProcessableCategory() {
        return TitleCategory.FOLLOWEE;
    }

    @Override
    protected Optional<Long> judgeTitleAcquire(KeeweTitleHeader header) {
        // 팔로워를 얻은 사람
        Long userId = Long.valueOf(header.getUserId());
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        Long followerCount = followQueryRepository.countFolloweeBy(user);
        return Arrays.stream(FollowingTitle.values())
                .sorted(Comparator.comparingLong(FollowingTitle::getStandard).reversed())
                .filter(it -> it.getStandard() <= followerCount)
                .limit(1)
                .findFirst()
                .map(FollowingTitle::getId);
    }
}
