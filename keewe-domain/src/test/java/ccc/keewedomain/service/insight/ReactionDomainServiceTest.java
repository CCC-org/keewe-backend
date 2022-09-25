package ccc.keewedomain.service.insight;

import ccc.keewedomain.KeeweDomainApplication;
import ccc.keewedomain.repository.insight.ReactionAggregationRepository;
import ccc.keeweinfra.KeeweInfraApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {KeeweDomainApplication.class, KeeweInfraApplication.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-domain.yml"})
@ActiveProfiles("local")
class ReactionDomainServiceTest {
    @Autowired
    private ReactionAggregationRepository reactionAggregationRepository;

    @Test
    @DisplayName("@Lock 테스트")
    void test1() {
        //TODO 테스트 작성...
    }
}