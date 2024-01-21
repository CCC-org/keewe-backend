package ccc.keewedomain.service.title;

import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.persistence.domain.title.enums.BookmarkTitle;
import ccc.keewedomain.persistence.repository.insight.BookmarkQueryRepository;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookmarkTitleAcquireProcessor extends AbstractTitleAcquireProcessor {
    private final BookmarkQueryRepository bookmarkQueryRepository;

    public BookmarkTitleAcquireProcessor(
            MQPublishService mqPublishService,
            TitleAchievementRepository titleAchievementRepository,
            UserDomainService userDomainService,
            UserRepository userRepository,
            TitleRepository titleRepository,
            NotificationCommandDomainService notificationCommandDomainService,
            BookmarkQueryRepository bookmarkQueryRepository
    ) {
        super(mqPublishService, titleAchievementRepository, userDomainService, userRepository, titleRepository, notificationCommandDomainService);
        this.bookmarkQueryRepository = bookmarkQueryRepository;
    }

    @Override
    public TitleCategory getProcessableCategory() {
        return TitleCategory.BOOKMARK;
    }

    @Override
    protected List<Long> judgeTitleAcquire(KeeweTitleHeader header) {
        List<Long> result = new ArrayList<>();
        Long userId = Long.valueOf(header.getUserId());
        Long current = bookmarkQueryRepository.countByUserId(userId);

        if (current.equals(1L)) {
            result.add(BookmarkTitle.북마크_최초.getId());
        }
        return result;
    }
}
