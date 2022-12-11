package ccc.keewedomain.service.insight;

import ccc.keewedomain.KeeweDomainApplication;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.dto.insight.ReactionIncrementDto;
import ccc.keewedomain.dto.user.UserSignUpDto;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.utils.DatabaseCleaner;
import ccc.keeweinfra.KeeweInfraApplication;
import ccc.keeweinfra.service.MQPublishService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static ccc.keewecore.consts.KeeweConsts.INSIGHT_REACT_EXCHANGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {KeeweDomainApplication.class, KeeweInfraApplication.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-domain.yml"})
@ActiveProfiles("test")
public class ReactionDomainServiceTest {

    @Autowired
    ReactionDomainService reactionDomainService;

    @Autowired
    InsightDomainService insightDomainService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DatabaseCleaner databaseCleaner;

    @MockBean
    MQPublishService mqPublishService;

    @Captor
    ArgumentCaptor<ReactionIncrementDto> rIDCaptor;

    @Captor
    ArgumentCaptor<String> strCaptor;

    User user = User.from(UserSignUpDto.of("vendorId", VendorType.NAVER, "boseong844@naver.com", null, null));

    @BeforeEach
    void setup() {
        userRepository.save(user);
    }

    @AfterEach
    void clean() {
        databaseCleaner.execute();
    }

    @Test
    @DisplayName("반응 테스트")
    void react() {
        InsightCreateDto insightCreateDto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", false, null);
        Insight insight = insightDomainService.create(insightCreateDto);
        Long insightId = insight.getId();
        ReactionType reactionType = ReactionType.CLAP;
        Long value = 1L;
        ReactionIncrementDto reactionIncrementDto = ReactionIncrementDto.of(insightId, user.getId(), reactionType, value);

        ReactionDto reactionDto = reactionDomainService.react(reactionIncrementDto);
        verify(mqPublishService).publish(strCaptor.capture(), rIDCaptor.capture());

        assertThat(reactionDto.getCount()).isEqualTo(value);
        assertThat(reactionDto.getReactionType()).isEqualTo(reactionType);
        assertThat(strCaptor.getValue()).isEqualTo(INSIGHT_REACT_EXCHANGE);
        assertThat(rIDCaptor.getValue().getInsightId()).isEqualTo(insightId);
    }
}
