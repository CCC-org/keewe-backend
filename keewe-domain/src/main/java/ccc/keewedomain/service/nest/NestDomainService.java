package ccc.keewedomain.service.nest;


import ccc.keewedomain.domain.nest.Nest;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.repository.nest.NestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class NestDomainService {
    private final NestRepository nestRepository;

    public Long save(Profile profile) {
        return nestRepository.save(Nest.of(profile)).getId();
    }
}
