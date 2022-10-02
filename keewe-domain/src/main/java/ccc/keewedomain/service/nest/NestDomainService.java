package ccc.keewedomain.service.nest;


import ccc.keewedomain.persistence.repository.nest.NestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class NestDomainService {
    private final NestRepository nestRepository;

//    public Long save(Profile profile) {
//        return nestRepository.save(Nest.of(profile)).getId();
//    }
}
