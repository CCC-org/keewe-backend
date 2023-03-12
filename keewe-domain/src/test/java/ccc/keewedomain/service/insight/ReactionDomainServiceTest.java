package ccc.keewedomain.service.insight;

import ccc.keewedomain.KeeweDomainApplication;
import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.repository.insight.CReactionCountRepository;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.dto.insight.ReactionIncrementDto;
import ccc.keewedomain.dto.user.UserSignUpDto;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.insight.command.InsightCommandDomainService;
import ccc.keewedomain.utils.DatabaseCleaner;
import ccc.keeweinfra.KeeweInfraApplication;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static ccc.keewecore.consts.KeeweConsts.INSIGHT_REACT_EXCHANGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {KeeweDomainApplication.class, KeeweInfraApplication.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-domain.yml"})
@ActiveProfiles("test")
public class ReactionDomainServiceTest {

    @Autowired
    ReactionDomainService reactionDomainService;

    @Autowired
    InsightCommandDomainService insightCommandDomainService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReactionAggregationRepository reactionAggregationRepository;

    @Autowired
    CReactionCountRepository cReactionCountRepository;

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

    // TODO : local cache clear
    @AfterEach
    void clean() {
        databaseCleaner.execute();
    }

    @Test
    @DisplayName("반응 테스트")
    void react() {
        InsightCreateDto insightCreateDto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", false, null);
        Insight insight = insightCommandDomainService.create(insightCreateDto);
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

    @Test
    @DisplayName("applyReact 테스트 (반응 메세지 컨슈머 호출 함수)")
    void apply_react_test() {
        InsightCreateDto insightCreateDto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", false, null);
        Insight insight = insightCommandDomainService.create(insightCreateDto);
        Long insightId = insight.getId();
        ReactionType reactionType = ReactionType.CLAP;
        Long value = 1L;
        ReactionIncrementDto reactionIncrementDto = ReactionIncrementDto.of(insightId, user.getId(), reactionType, value);

        reactionDomainService.applyReact(reactionIncrementDto);

        ReactionAggregationGetDto dto = reactionAggregationRepository.findDtoByInsightId(insightId);
        assertThat(dto.getByType(reactionType)).isEqualTo(value);
        assertThat(dto.getEyes()).isEqualTo(0L);

        CReactionCount cnt = cReactionCountRepository.findByIdWithMissHandle(insightId, () -> dto);
        ReactionAggregationGetDto byCnt = ReactionAggregationGetDto.createByCnt(cnt);
        assertEquals(dto, byCnt);
    }
}
