package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.insight.Drawer;
import ccc.keewedomain.repository.insight.DrawerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawerDomainService {

    private final DrawerRepository drawerRepository;

    public Drawer getById(Long id) {
        return drawerRepository.findByIdAndAndDeletedFalse(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR440));
    }
}
