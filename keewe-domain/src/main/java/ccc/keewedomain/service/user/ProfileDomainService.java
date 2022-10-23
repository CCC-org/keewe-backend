package ccc.keewedomain.service.user;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.user.FollowCheckDto;
import ccc.keewedomain.dto.user.FollowToggleDto;
import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.dto.user.OnboardDto;
import ccc.keewedomain.persistence.domain.user.id.FollowId;
import ccc.keewedomain.persistence.repository.user.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileDomainService {
    private final UserDomainService userDomainService;
    private final FollowRepository followRepository;

    public User onboard(OnboardDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        user.initProfile(dto.getNickname(), dto.getInterests());
        return user;
    }

    public boolean toggleFollowership(FollowToggleDto followDto) {
        validateSelfFollowing(followDto);

        User user = userDomainService.getUserByIdOrElseThrow(followDto.getUserId());
        User target = userDomainService.getUserByIdOrElseThrow(followDto.getTargetId());

        FollowId followId = FollowId.of(target.getId(), user.getId());
        followRepository.findById(followId)
                        .ifPresentOrElse(
                                follow -> {
                                    log.info("[PDS::toggleFollowership] Found Relation followee {}, follower {}", user.getId(), target.getId());
                                    follow.removeRelation(target, user);
                                    followRepository.delete(follow);
                                },
                                () -> {
                                    log.info("[PDS::toggleFollowership] Not Found Relation followee {}, follower {}", user.getId(), target.getId());
                                    Follow relation = Follow.makeRelation(target, user);
                                    followRepository.save(relation);
                                }
                        );

        return isFollowing(FollowCheckDto.of(target.getId(), user.getId()));
    }

    public boolean isFollowing(FollowCheckDto followCheckDto) {
        return followRepository.existsById(FollowId.of(followCheckDto.getTargetId(), followCheckDto.getUserId()));
    }

    private void validateSelfFollowing(FollowToggleDto followDto) {
        if(followDto.getTargetId() == followDto.getUserId()) {
            throw new KeeweException(KeeweRtnConsts.ERR446);
        }
    }
}
