package ccc.keewestatistics;

import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.domain.user.enums.VendorType;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.user.UserSignUpDto;
import ccc.keewedomain.repository.insight.InsightRepository;
import ccc.keewedomain.repository.user.UserRepository;
import ccc.keewedomain.service.insight.InsightDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("local")
public class InsightViewIncrementTest {

    @Autowired
    private InsightDomainService insightDomainService;

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private InsightRepository insightRepository;

    @Autowired
    private UserRepository userRepository;

    private Long insightId;
    private Long userId;

    @AfterEach
    void rollback() {
        insightRepository.deleteById(insightId);
        userRepository.deleteById(userId);
    }

    @Test
    void 멀티쓰레드_조회수_증가_성공() {
        User user = createUser();

        InsightCreateDto insightCreateDto = InsightCreateDto.of(user.getId()
                , "컨텐츠!"
                , "www.kakaocorp.com"
                , false
                , null);

        Insight insight = insightDomainService.create(insightCreateDto);

        userId = user.getId();
        insightId = insight.getId();

        int threadCnt = 5;

        List<Thread> threads = IntStream.rangeClosed(1, threadCnt)
                .mapToObj(i -> new Thread(doIncrement(insightId, threadCnt)))
                .collect(Collectors.toList());

        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Optional<Insight> viewedInsight = insightRepository.findById(insightId);

        Assertions.assertThat(viewedInsight.get().getView())
                .isEqualTo(threadCnt * threadCnt);

    }

    private User createUser() {
        UserSignUpDto user = UserSignUpDto.of(String.valueOf(UUID.randomUUID())
                , VendorType.KAKAO
                , "h0song@kakaocorp.com"
                , null
                , null);

        return userDomainService.save(user);
    }


    private Runnable doIncrement(Long insightId, int threadCnt) {
        return () -> {
            for(int i=0; i<threadCnt; i++)
                insightDomainService.incrementViewCount(insightId);
        };
    }

}
