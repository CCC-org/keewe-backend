package ccc.keewestatistics;


import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.insight.ReactionIncrementDto;
import ccc.keewedomain.dto.user.UserSignUpDto;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.ReactionAggregation;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.insight.id.ReactionAggregationId;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.service.insight.ReactionDomainService;
import ccc.keewedomain.service.insight.command.InsightCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class InsightReactTest {
    @Autowired
    private InsightCommandDomainService insightCommandDomainService;
    @Autowired
    private UserDomainService userDomainService;
    @Autowired
    private ReactionDomainService reactionDomainService;
    @Autowired
    private CReactionCountRepository cReactionCountRepository;
    @Autowired
    private ReactionAggregationRepository reactionAggregationRepository;

    private Long userId;
    private Long insightId;

    @Test
    @DisplayName("인사이트 반응 멀티 쓰레드 테스트")
    void test1() {
        User user = createUser();
        Insight insight = createInsight(user);
        userId = user.getId();
        insightId = insight.getId();

        int threadCnt = 10;

        List<Thread> threads = IntStream.rangeClosed(1, threadCnt)
                .mapToObj(i -> new Thread(doApplyReact(threadCnt)))
                .collect(Collectors.toList());

        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Optional<CReactionCount> cCount = cReactionCountRepository.findById(insightId);
        Optional<ReactionAggregation> dbCount = reactionAggregationRepository.findById(new ReactionAggregationId(insightId, ReactionType.CLAP));

        Assertions.assertThat(cCount.get().getClap()).isEqualTo(threadCnt * threadCnt);
        Assertions.assertThat(cCount.get().getClap()).isEqualTo(dbCount.get().getCount());
    }

    private Runnable doApplyReact(int threadCnt) {
        return () -> {
            for (int i = 0; i < threadCnt; i++)
                reactionDomainService.applyReact(ReactionIncrementDto.of(
                        userId, insightId, ReactionType.CLAP, 1L
                ));
        };
    }

    private Insight createInsight(User user) {
        return insightCommandDomainService.create(InsightCreateDto.of(
                user.getId(),
                "컨텐츠!",
                "www.kakaocorp.com",
                false,
                null
        ));
    }

    private User createUser() {
        return userDomainService.save(UserSignUpDto.of(
                        String.valueOf(UUID.randomUUID()),
                        VendorType.KAKAO,
                        "h0song@kakaocorp.com",
                        null,
                        null
                )
        );
    }
}
