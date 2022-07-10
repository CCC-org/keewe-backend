package ccc.keeweapi.service.user;

import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmailOrElseThrow(email);
    }
}
