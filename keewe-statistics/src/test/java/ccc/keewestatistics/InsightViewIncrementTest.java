package ccc.keewestatistics;


import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.domain.user.enums.VendorType;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.user.UserSignUpDto;
import ccc.keewedomain.repository.insight.InsightRepository;
import ccc.keewedomain.service.insight.InsightDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
public class InsightViewIncrementTest {

    @Autowired
    private InsightDomainService insightDomainService;

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private InsightRepository insightRepository;

    @Test
    @Commit
    void incrementWithMultiThread() {


        Insight insight = saveInsight();

        int threadCnt = 5;

        Insight finalInsight = insight; // for 람다 캡쳐링
        List<Thread> threads = IntStream.rangeClosed(1, threadCnt)
                .mapToObj(i -> new Thread(doIncrement(finalInsight.getId(), threadCnt)))
                .collect(Collectors.toList());

        threads.forEach(Thread::start);
//        threads.forEach(thread -> {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//        Insight viewedInsight = insightRepository.findByIdOrElseThrow(insight.getId());
//
//        System.out.println(viewedInsight.getId());
//
//        Assertions.assertThat(viewedInsight.getView())
//                .isEqualTo(threadCnt * threadCnt);

    }

    @Transactional(propagation = Propagation.NESTED)
    Insight saveInsight() {
        User user = createUser();

        InsightCreateDto insightCreateDto = InsightCreateDto.of(user.getId()
                , "컨텐츠!"
                , "www.kakaocorp.com"
                , false
                , null);

        return insightDomainService.create(insightCreateDto);
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
