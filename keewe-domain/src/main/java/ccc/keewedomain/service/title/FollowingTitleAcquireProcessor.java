package ccc.keewedomain.service.title;

import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.user.FollowQueryRepository;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class FollowingTitleAcquireProcessor extends AbstractTitleAcquireProcessor {
    private final UserDomainService userDomainService;
    private final FollowQueryRepository followQueryRepository;

    protected FollowingTitleAcquireProcessor(
            MQPublishService mqPublishService,
            TitleAchievementRepository titleAchievementRepository,
            UserDomainService userDomainService,
            UserRepository userRepository,
            TitleRepository titleRepository,
            FollowQueryRepository followQueryRepository,
            NotificationCommandDomainService notificationCommandDomainService
    ) {
        super(mqPublishService, titleAchievementRepository, userDomainService, userRepository, titleRepository, notificationCommandDomainService);
        this.userDomainService = userDomainService;
        this.followQueryRepository = followQueryRepository;
    }

    @Override
    public TitleCategory getProcessableCategory() {
        return TitleCategory.FOLLOWING;
    }

    @Override
    protected List<Long> judgeTitleAcquire(KeeweTitleHeader header) {
        Long userId = Long.valueOf(header.getUserId());
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        Long followeeCount = followQueryRepository.countFollowersBy(user);
        return followeeCount >= 40 ? List.of(3003L) : List.of();
    }
}
