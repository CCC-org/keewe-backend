package ccc.keewedomain.service.challenge;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.consts.LockType;
import ccc.keewecore.exception.KeeweException;
import ccc.keewecore.utils.RedisLockUtils;
import ccc.keewedomain.dto.challenge.ChallengeCreateDto;
import ccc.keewedomain.dto.challenge.ChallengeParticipateDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.challenge.ChallengeParticipationQueryRepository;
import ccc.keewedomain.persistence.repository.challenge.ChallengeParticipationRepository;
import ccc.keewedomain.persistence.repository.challenge.ChallengeQueryRepository;
import ccc.keewedomain.persistence.repository.challenge.ChallengeRepository;
import ccc.keewedomain.service.user.UserDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import static ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus.CHALLENGING;

@Service
@RequiredArgsConstructor
public class ChallengeDomainService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;
    private final ChallengeParticipationRepository challengeParticipationRepository;
    private final ChallengeParticipationQueryRepository challengeParticipationQueryRepository;
    private final UserDomainService userDomainService;
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
            Challenge challenge = this.getByIdOrElseThrow(dto.getChallengeId());
            return challenge.participate(challenger, dto.getMyTopic(), dto.getInsightPerWeek(), dto.getDuration());
        });
    }

    public Challenge getByIdOrElseThrow(Long id) {
        return challengeRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR430));
    }

    public boolean checkParticipation(Long userId) {
        return challengeParticipationQueryRepository.existsByChallengerIdAndStatus(userId, CHALLENGING);
    }

    public ChallengeParticipation getCurrentChallengeParticipation(User challenger) {
        return findCurrentChallengeParticipation(challenger).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR432));
    }

    public Optional<ChallengeParticipation> findCurrentParticipationWithChallenge(Long challengerId) {
        return challengeParticipationQueryRepository.findByChallengerIdAndStatusWithChallenge(challengerId, CHALLENGING);
    }

    public Optional<ChallengeParticipation> findCurrentChallengeParticipation(User challenger) {
        return challengeParticipationRepository.findByChallengerAndStatusAndDeletedFalse(challenger, CHALLENGING);
    }

    public Map<String, Long> getRecordCountPerDate(ChallengeParticipation participation) {
        LocalDate startDate = participation.getStartDateOfThisWeek();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        return challengeParticipationQueryRepository.getRecordCountPerDate(participation, startDateTime, startDateTime.plusDays(7L));
    }

    public List<Challenge> getSpecifiedNumberOfRecentChallenge(int size) {
        return challengeQueryRepository.getSpecifiedNumberOfChallenge(size);
    }

    public Long countParticipatingUser(Challenge challenge) {
        return challengeParticipationQueryRepository.countByChallengeAndStatus(challenge, CHALLENGING);
    }

    public Long countFinishedParticipation(User user) {
        return challengeParticipationRepository.countByChallengerAndStatusNot(user, CHALLENGING);
    }

    public List<ChallengeParticipation> getFinishedParticipation(User user, Long size) {
        return challengeParticipationQueryRepository.findFinishedParticipation(user, size);
    }

    @Transactional(readOnly = true)
    public List<ChallengeParticipation> findTogetherChallengeParticipations(Challenge challenge, User user) {
        return challengeParticipationQueryRepository.findFollowingChallengerParticipations(challenge, user);
    }

    private void exitCurrentChallengeIfExist(User challenger) {
        findCurrentChallengeParticipation(challenger).ifPresent(ChallengeParticipation::cancel);
    }
}
