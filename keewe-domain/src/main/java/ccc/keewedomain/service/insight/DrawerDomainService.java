package ccc.keewedomain.service.insight;

import ccc.keewedomain.domain.insight.Drawer;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.insight.DrawerCreateDto;
import ccc.keewedomain.repository.insight.DrawerRepository;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrawerDomainService {

    private final DrawerRepository drawerRepository;

    private final UserDomainService userDomainService;

    public Drawer create(DrawerCreateDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        Drawer drawer = Drawer.of(user, dto.getName());
        return drawerRepository.save(drawer);
    }

    public Optional<Drawer> findById(Long id) {
        return drawerRepository.findByIdAndAndDeletedFalse(id);
    }

}
