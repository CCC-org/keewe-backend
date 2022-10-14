package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.KeeweDomainApplication;
import ccc.keewedomain.dto.insight.DrawerCreateDto;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.user.UserSignUpDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.common.Link;
import ccc.keewedomain.persistence.domain.insight.Drawer;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import ccc.keewedomain.persistence.repository.challenge.ChallengeParticipationRepository;
import ccc.keewedomain.persistence.repository.challenge.ChallengeRepository;
import ccc.keewedomain.persistence.repository.insight.DrawerRepository;
import ccc.keewedomain.persistence.repository.insight.InsightQueryRepository;
import ccc.keewedomain.persistence.repository.insight.InsightRepository;
import ccc.keewedomain.persistence.repository.insight.ReactionAggregationRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.utils.DatabaseCleaner;
import ccc.keeweinfra.KeeweInfraApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {KeeweDomainApplication.class, KeeweInfraApplication.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-domain.yml"})
@ActiveProfiles("test")
public class InsightDomainServiceTest {

    @Autowired
    InsightDomainService insightDomainService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InsightRepository insightRepository;

    @Autowired
    InsightQueryRepository insightQueryRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    ChallengeParticipationRepository challengeParticipationRepository;

    @Autowired
    ReactionAggregationRepository reactionAggregationRepository;

    @Autowired
    DrawerRepository drawerRepository;

    @Autowired
    DatabaseCleaner databaseCleaner;

    User user = User.from(UserSignUpDto.of("vendorId", VendorType.NAVER, "boseong844@naver.com", null, null));
    Insight insight = Insight.of(user, null, null, "인사이트 내용", Link.of("https://naver.com"), false);
    Challenge challenge = Challenge.of(user, "챌린지", "개발", "챌린지의 소개입니다.");
    ChallengeParticipation challengeParticipation = challenge.participate(user, "참가자 토픽", 2, 3);

    @BeforeEach
    void setup() {
        userRepository.save(user);
        insightRepository.save(insight);
        challengeRepository.save(challenge);
    }

    @AfterEach
    void clean() {
        databaseCleaner.execute();
    }

    @Test
    @DisplayName("챌린지 기록 없이 인사이트 생성")
    void create_without_participate() {
        InsightCreateDto dto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", false, null);
        Insight insight = insightDomainService.create(dto);

        assertAll(
                () -> assertThat(insight.isValid()).isFalse(),
                () -> assertThat(insightRepository.findById(insight.getId())).isPresent()
        );
    }

    @Test
    @DisplayName("챌린지 기록하면서 인사이트 생성")
    void create_with_participate() {
        InsightCreateDto dto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", true, null);
        Insight newInsight = insightDomainService.create(dto);

        assertAll(
                () -> assertThat(newInsight.isValid()).isTrue(),
                () -> assertThat(insightRepository.findById(newInsight.getId())).isPresent(),
                () -> assertThat(insightQueryRepository.countValidForParticipation(newInsight.getChallengeParticipation()))
                        .isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("이번 주 목표 기록 완료 시 기록 불가능")
    void valid_false_if_completed() {
        List<InsightCreateDto> dtos = List.of(
                InsightCreateDto.of(user.getId(), "인사이트 내용1", "https://comic.naver.com", true, null),
                InsightCreateDto.of(user.getId(), "인사이트 내용2", "https://comic.naver.com", true, null),
                InsightCreateDto.of(user.getId(), "인사이트 내용3", "https://comic.naver.com", true, null)
        );

        List<Insight> insights = dtos.stream()
                .map(dto -> insightDomainService.create(dto))
                .collect(Collectors.toList());

        Insight insight1 = insights.get(0);
        Insight insight2 = insights.get(1);
        Insight insight3 = insights.get(2);

        assertAll(
                () -> assertThat(insight1.isValid()).isTrue(),
                () -> assertThat(insight2.isValid()).isTrue(),
                () -> assertThat(insight3.isValid()).isFalse(),
                () -> assertThat(insightQueryRepository.countValidForParticipation(insight3.getChallengeParticipation()))
                .isEqualTo(2L)
        );
    }

    @Test
    @DisplayName("서랍이 존재하지 않으면 실패")
    void throw_if_drawer_not_exist() {
        InsightCreateDto dto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", true, 9999L);

        assertThatThrownBy(() -> insightDomainService.create(dto))
                .isExactlyInstanceOf(KeeweException.class)
                .hasMessage(KeeweRtnConsts.ERR440.getDescription());
    }

    @Test
    @DisplayName("자신의 서랍이 아니면 실패")
    void throw_if_not_owner_of_drawer() {
        User other = User.from(UserSignUpDto.of("vendorId222", VendorType.NAVER, "boseong844@naver.com", null, null));
        userRepository.save(other);
        Drawer drawer = drawerRepository.save(Drawer.of(other, "서랍"));

        InsightCreateDto dto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", true, drawer.getId());
        assertThatThrownBy(() -> insightDomainService.create(dto))
                .isExactlyInstanceOf(KeeweException.class)
                .hasMessage(KeeweRtnConsts.ERR444.getDescription());
    }
}
