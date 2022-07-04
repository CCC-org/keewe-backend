package ccc.keewedomain.service.user;

import ccc.keewedomain.repository.user.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public String createLink() {
        return "";
    }
}
