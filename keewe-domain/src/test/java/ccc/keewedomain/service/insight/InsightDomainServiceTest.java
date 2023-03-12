package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.KeeweDomainApplication;
import ccc.keewedomain.dto.insight.InsightCreateDto;
import ccc.keewedomain.dto.user.FollowToggleDto;
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
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.insight.command.InsightCommandDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.ProfileDomainService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = {KeeweDomainApplication.class, KeeweInfraApplication.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-domain.yml"})
@ActiveProfiles("test")
public class InsightDomainServiceTest {

    @Autowired
    InsightQueryDomainService insightQueryDomainService;

    @Autowired
    InsightCommandDomainService insightCommandDomainService;

    @Autowired
    ProfileDomainService profileDomainService;

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

    // TODO : redis 캐시 저장 시 rollback
    @AfterEach
    void clean() {
        databaseCleaner.execute();
    }

    @Test
    @DisplayName("챌린지 기록 없이 인사이트 생성")
    void create_without_participate() {
        //given
        InsightCreateDto dto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", false, null);
        //when
        Insight insight = insightCommandDomainService.create(dto);

        //then
        assertAll(
                () -> assertThat(insight.isValid()).isFalse(),
                () -> assertThat(insightRepository.findById(insight.getId())).isPresent()
        );
    }

    @Test
    @DisplayName("챌린지 기록하면서 인사이트 생성")
    void create_with_participate() {
        //given
        InsightCreateDto dto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", true, null);
        //when
        Insight newInsight = insightCommandDomainService.create(dto);

        //then
        assertAll(
                () -> assertThat(newInsight.isValid()).isTrue(),
                () -> assertThat(insightRepository.findById(newInsight.getId())).isPresent(),
                () -> assertThat(insightQueryRepository.countValidByParticipation(newInsight.getChallengeParticipation()))
                        .isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("이번 주 목표 기록 완료 시 기록 불가능")
    void valid_false_if_completed() {
        //given
        List<InsightCreateDto> dtos = List.of(
                InsightCreateDto.of(user.getId(), "인사이트 내용1", "https://comic.naver.com", true, null),
                InsightCreateDto.of(user.getId(), "인사이트 내용2", "https://comic.naver.com", true, null),
                InsightCreateDto.of(user.getId(), "인사이트 내용3", "https://comic.naver.com", true, null)
        );

        //when
        List<Insight> insights = dtos.stream()
                .map(dto -> insightCommandDomainService.create(dto))
                .collect(Collectors.toList());

        Insight insight1 = insights.get(0);
        Insight insight2 = insights.get(1);
        Insight insight3 = insights.get(2);

        //then
        assertAll(
                () -> assertThat(insight1.isValid()).isTrue(),
                () -> assertThat(insight2.isValid()).isTrue(),
                () -> assertThat(insight3.isValid()).isFalse(),
                () -> assertThat(insightQueryRepository.countValidByParticipation(insight3.getChallengeParticipation()))
                .isEqualTo(2L)
        );
    }

    @Test
    @DisplayName("서랍이 존재하지 않으면 실패")
    void throw_if_drawer_not_exist() {
        //given
        InsightCreateDto dto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", true, 9999L);

        //when, then
        assertThatThrownBy(() -> insightCommandDomainService.create(dto))
                .isExactlyInstanceOf(KeeweException.class)
                .hasMessage(KeeweRtnConsts.ERR440.getDescription());
    }

    @Test
    @DisplayName("자신의 서랍이 아니면 실패")
    void throw_if_not_owner_of_drawer() {
        //given
        User other = User.from(UserSignUpDto.of("vendorId222", VendorType.NAVER, "boseong844@naver.com", null, null));
        userRepository.save(other);
        Drawer drawer = drawerRepository.save(Drawer.of(other, "서랍"));
        InsightCreateDto dto = InsightCreateDto.of(user.getId(), "인사이트 내용", "https://comic.naver.com", true, drawer.getId());

        //when, then
        assertThatThrownBy(() -> insightCommandDomainService.create(dto))
                .isExactlyInstanceOf(KeeweException.class)
                .hasMessage(KeeweRtnConsts.ERR444.getDescription());
    }

    @Test
    @DisplayName("홈 화면 조회 팔로우 필터 테스트")
    @Transactional
    void get_for_home_follow_filter() {
        Boolean follow = true;
        User other = User.from(UserSignUpDto.of("vendorId222", VendorType.NAVER, "boseong844@naver.com", null, null));
        userRepository.save(other);

        // user(other) follows no one
        int noOne = insightQueryDomainService.getInsightsForHome(other, CursorPageable.of(Long.MAX_VALUE, 10L), follow).size();
        assertThat(noOne).isEqualTo(0);

        // user(other) follows user
        profileDomainService.toggleFollowership(FollowToggleDto.of(other.getId(), user.getId()));
        int followUser = insightQueryDomainService.getInsightsForHome(other, CursorPageable.of(Long.MAX_VALUE, 10L), follow).size();
        assertThat(followUser).isEqualTo(1);
    }
}
