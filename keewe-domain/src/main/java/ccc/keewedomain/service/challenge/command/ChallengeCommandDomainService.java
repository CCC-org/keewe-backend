package ccc.keewedomain.service.challenge.command;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.consts.LockType;
import ccc.keewecore.exception.KeeweException;
import ccc.keewecore.utils.RedisLockUtils;
import ccc.keewedomain.dto.challenge.ChallengeCreateDto;
import ccc.keewedomain.dto.challenge.ChallengeParticipateDto;
import ccc.keewedomain.dto.challenge.ParticipationUpdateDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.challenge.ChallengeParticipationRepository;
import ccc.keewedomain.persistence.repository.challenge.ChallengeRepository;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChallengeCommandDomainService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipationRepository challengeParticipationRepository;
    private final ChallengeQueryDomainService challengeQueryDomainService;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final UserDomainService userDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final RedisLockUtils redisLockUtils;

    @Transactional
    public Challenge save(ChallengeCreateDto dto) {
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        Challenge challenge = Challenge.of(writer, dto.getName(), dto.getInterest(), dto.getIntroduction());
        return challengeRepository.save(challenge);
    }

    @Transactional
    public List<Challenge> saveAll(List<Challenge> challenges) {
        return challengeRepository.saveAll(challenges);
    }

    @Transactional
    public ChallengeParticipation participate(ChallengeParticipateDto dto) {
        return redisLockUtils.executeWithLock(LockType.CHALLENGE_PARTICIPATE, dto.getChallengerId().toString(), 3L, () -> {
            User challenger = userDomainService.getUserByIdOrElseThrow(dto.getChallengerId());
            Assert.isTrue(challenger.isActive(), "정상 상태의 유저만 챌린지에 참여할 수 있어요.");
            Assert.isTrue(!challenger.isDeleted(), "탈퇴한 사용자는 챌린지에 참여할 수 없어요.");
            this.exitCurrentChallengeIfExist(challenger);
            Challenge challenge = challengeQueryDomainService.getByIdOrElseThrow(dto.getChallengeId());
            ChallengeParticipation participate = challenge.participate(challenger, dto.getMyTopic(), dto.getInsightPerWeek(), dto.getDuration());
            challengeRepository.save(challenge);
            challengeParticipationRepository.save(participate);
            log.info("[ChallengeCommandDomainService] 챌린지 참여 완료 - userId({}), challengeId({})", challenger.getId(), challenge.getId());
            return participate;
        });
    }

    @Transactional
    public void updateParticipation(ParticipationUpdateDto dto) {
        ChallengeParticipation participation = challengeParticipateQueryDomainService.getCurrentParticipationByUserId(dto.getUserId());
        validateInsightPerWeek(participation, dto.getInsightPerWeek());
        participation.update(dto.getMyTopic(), dto.getInsightPerWeek(), dto.getDuration());
    }

    // 남은 일자 < insightPerWeek 확인
    private void validateInsightPerWeek(ChallengeParticipation participation, int insightPerWeek) {
        LocalDateTime start = participation.getStartDateOfThisWeek().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        Long thisWeekRecordCount = insightQueryDomainService.countInsightCreatedAtBetween(participation, start, end);
        int remainDays = 7 - Period.between(start.toLocalDate(), end.toLocalDate()).getDays();
        if(insightQueryDomainService.isTodayRecorded(participation.getChallenger())) {
            remainDays -= 1;
        }
        if(insightPerWeek - thisWeekRecordCount > remainDays) {
            throw new KeeweException(KeeweRtnConsts.ERR434);
        }
    }

    @Transactional
    public void exitCurrentChallengeIfExist(User challenger) {
        challengeParticipateQueryDomainService.findCurrentChallengeParticipation(challenger)
            .ifPresent(participate -> {
                participate.cancel();
                challengeParticipationRepository.save(participate);
            });
    }
}
