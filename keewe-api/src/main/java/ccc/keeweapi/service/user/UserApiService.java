package ccc.keeweapi.service.user;

import ccc.keeweapi.component.UserAssembler;
import ccc.keeweapi.config.security.jwt.JwtUtils;
import ccc.keeweapi.dto.user.UserSignUpResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.aop.annotations.FLogging;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.TitleCategory;
import ccc.keewecore.utils.KeeweStringUtils;
import ccc.keewecore.utils.KeeweTitleHeader;
import ccc.keewedomain.dto.user.UserTokenRegisterDto;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import ccc.keewedomain.service.challenge.command.ChallengeCommandDomainService;
import ccc.keewedomain.service.insight.command.CommentCommandDomainService;
import ccc.keewedomain.service.insight.command.InsightCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keewedomain.service.user.command.ProfileCommandDomainService;
import ccc.keewedomain.service.user.command.UserCommandDomainService;
import ccc.keeweinfra.dto.OauthResponse;
import ccc.keeweinfra.service.messagequeue.MQPublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserApiService {
    private final UserDomainService userDomainService;
    private final UserCommandDomainService userCommandDomainService;
    private final UserAssembler userAssembler;
    private final JwtUtils jwtUtils;
    private final MQPublishService mqPublishService;
    private final ProfileCommandDomainService profileCommandDomainService;
    private final ChallengeCommandDomainService challengeCommandDomainService;
    private final InsightCommandDomainService insightCommandDomainService;
    private final CommentCommandDomainService commentCommandDomainService;

    @FLogging
    public <T extends OauthResponse> UserSignUpResponse signupWithOauth(String code, VendorType vendorType) {
        T account = userDomainService.getOauthProfile(code, vendorType);

        Optional<User> userOps = userDomainService.getUserByVendorIdAndVendorType(account.getId(), vendorType);
        if(userOps.isPresent()) {
            log.info("[UAS::signupWithOauth] 로그인 완료 - email({})", account.getEmail());
            User user = userOps.get();
            return userAssembler.toUserSignUpResponse(user, user.isActive(), getToken(user.getId()));
        }

        User user = signUpWithOauth(
                account.getId()
                , vendorType
                , KeeweStringUtils.getOrDefault(account.getEmail(), "")
        );

        this.afterTheFirstSignUp(user);
        String accessToken = getToken(user.getId());
        userCommandDomainService.registerToken(UserTokenRegisterDto.of(user.getId(), accessToken, null, null));
        log.info("[UAS::signupWithOauth] 회원가입 완료 - email({})", account.getEmail());
        return userAssembler.toUserSignUpResponse(user, false, accessToken);
    }

    public String getToken(Long userId) {
        return jwtUtils.createToken(userId, List.of());
    }

    @Transactional
    public void withdraw() {
        User user = userCommandDomainService.withdraw(SecurityUtil.getUserId());
        insightCommandDomainService.deleteAll(user.getId());
        profileCommandDomainService.removeAllRelationsBy(user);
        challengeCommandDomainService.exitCurrentChallengeIfExist(user);
        commentCommandDomainService.deleteAll(user);
        log.info("[UAS::withdraw] 회원 탈퇴 완료 - userId({})", user.getId());
    }

    private User signUpWithOauth(String vendorId, VendorType vendorType, String email) {
        return userDomainService.save(userAssembler.toUserSignUpDto(vendorId, vendorType, email));
    }

    private void afterTheFirstSignUp(User user) {
        try {
            Message message = MessageBuilder.withBody(new byte[0]).build();
            KeeweTitleHeader header = KeeweTitleHeader.of(TitleCategory.SIGNUP, String.valueOf(user.getId()));
            log.info("[UAS::afterSignUp] 타이틀 이벤트 발행 - category({}), userId({})", header.getCategory(), header.getUserId());
            mqPublishService.publish(KeeweConsts.DEFAULT_ROUTING_KEY, KeeweConsts.TITLE_STAT_QUEUE, message, header::toMessageWithHeader);
        } catch (Throwable t) {
            log.warn("[UAS::afterSignUp] 최초 회원가입 후 작업 실패 - userId({})", user.getId(), t);
        }
    }
}
