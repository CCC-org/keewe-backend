package ccc.keewedomain.service.user.command;

import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCommandDomainService {
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    @Transactional
    public User withdraw(Long userId) {
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        user.withdraw();
        return userRepository.save(user);
    }
}
