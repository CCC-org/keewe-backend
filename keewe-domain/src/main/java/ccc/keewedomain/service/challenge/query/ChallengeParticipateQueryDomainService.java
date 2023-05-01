package ccc.keewedomain.service.challenge.query;

import static ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus.CHALLENGING;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.challenge.ChallengeParticipationQueryRepository;
import ccc.keewedomain.persistence.repository.challenge.ChallengeParticipationRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChallengeParticipateQueryDomainService {

    private final ChallengeParticipationRepository challengeParticipationRepository;
    private final ChallengeParticipationQueryRepository challengeParticipationQueryRepository;

    public boolean checkParticipation(Long userId) {
        return challengeParticipationQueryRepository.existsByChallengerIdAndStatus(userId, CHALLENGING);
    }

    public ChallengeParticipation getCurrentChallengeParticipation(User user) {
        return findCurrentChallengeParticipation(user).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR432));
    }

    public ChallengeParticipation getCurrentParticipationByUserId(Long userId) {
        return findCurrentParticipationByUserId(userId).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR432));
    }

    public Optional<ChallengeParticipation> findCurrentParticipationByUserId(Long userId) {
        return challengeParticipationQueryRepository.findByUserIdAndStatus(userId, CHALLENGING);
    }

    public Optional<ChallengeParticipation> findCurrentChallengeParticipation(User user) {
        return challengeParticipationRepository.findByChallengerAndStatusAndDeletedFalse(user, CHALLENGING);
    }

    public Map<String, Long> getRecordCountPerDate(ChallengeParticipation participation) {
        LocalDate startDate = participation.getStartDateOfThisWeek();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        return challengeParticipationQueryRepository.getRecordCountPerDate(participation, startDateTime, startDateTime.plusDays(7L));
    }

    public Long countParticipatingUser(Challenge challenge) {
        return challengeParticipationQueryRepository.countByChallengeAndStatus(challenge, CHALLENGING);
    }

    public Long countFinishedParticipation(User user) {
        return challengeParticipationRepository.countByChallengerAndStatusNot(user, CHALLENGING);
    }

    @Transactional(readOnly = true)
    public List<ChallengeParticipation> findFriendsParticipations(Challenge challenge, User user, Pageable pageable) {
        return challengeParticipationQueryRepository.findByChallengeOrderByFollowing(challenge, user, pageable);
    }

    public List<ChallengeParticipation> paginateFinished(User challenger, CursorPageable<Long> cPage) {
        return challengeParticipationQueryRepository.paginateFinished(challenger, cPage);
    }

    public List<ChallengeParticipation> paginateFinished(CursorPageable<Long> cPage, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return challengeParticipationQueryRepository.paginateFinished(cPage, startDateTime, endDateTime);
    }
}
