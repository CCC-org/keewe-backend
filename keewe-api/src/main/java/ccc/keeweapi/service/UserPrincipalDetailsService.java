package ccc.keeweapi.service;

import ccc.keeweapi.config.UserPrincipal;
import ccc.keewedomain.repository.UserRepository;
import ccc.keewedomain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        if(user.isPresent()){
            return new UserPrincipal(user.get());
        } else {
            throw new RuntimeException(); //FIXME
        }

    }
}
