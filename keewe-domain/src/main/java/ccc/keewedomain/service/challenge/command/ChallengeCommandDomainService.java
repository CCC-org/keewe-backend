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
import ccc.keewedomain.persistence.repository.challenge.ChallengeRepository;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;

@RequiredArgsConstructor
@Service
public class ChallengeCommandDomainService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryDomainService challengeQueryDomainService;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final UserDomainService userDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final RedisLockUtils redisLockUtils;

    public Challenge save(ChallengeCreateDto dto) {
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        Challenge challenge = Challenge.of(writer, dto.getName(), dto.getInterest(), dto.getIntroduction());
        return challengeRepository.save(challenge);
    }

    public ChallengeParticipation participate(ChallengeParticipateDto dto) {
        return redisLockUtils.executeWithLock(LockType.CHALLENGE_PARTICIPATE, dto.getChallengerId().toString(), 3L, () -> {
            User challenger = userDomainService.getUserByIdOrElseThrow(dto.getChallengerId());
            exitCurrentChallengeIfExist(challenger);
            Challenge challenge = challengeQueryDomainService.getByIdOrElseThrow(dto.getChallengeId());
            return challenge.participate(challenger, dto.getMyTopic(), dto.getInsightPerWeek(), dto.getDuration());
        });
    }

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

    private void exitCurrentChallengeIfExist(User challenger) {
        challengeParticipateQueryDomainService.findCurrentChallengeParticipation(challenger).ifPresent(ChallengeParticipation::cancel);
    }
}
