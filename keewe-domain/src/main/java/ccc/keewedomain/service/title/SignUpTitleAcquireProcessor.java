package ccc.keewedomain.service.title;

import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.persistence.repository.user.TitleAchievementRepository;
import ccc.keewedomain.persistence.repository.user.TitleRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SignUpTitleAcquireProcessor extends AbstractTitleAcquireProcessor {
    protected SignUpTitleAcquireProcessor(
        MQPublishService mqPublishService,
        TitleAchievementRepository titleAchievementRepository,
        UserDomainService userDomainService,
        UserRepository userRepository,
        TitleRepository titleRepository
    ) {
        super(mqPublishService, titleAchievementRepository, userDomainService, userRepository, titleRepository);
    }

    @Override
    public TitleCategory getProcessableCategory() {
        return TitleCategory.SIGNUP;
    }

    @Override
    protected Optional<Long> judgeTitleAcquire(KeeweTitleHeader header) {
        // note. 회원가입 이벤트는 항상 최초에만 발행중
        return Optional.of(1000L);
    }
}
