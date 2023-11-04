package ccc.keewedomain.service.search;

import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.user.UserQueryRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSearcher implements Searcher<User> {

    private final UserQueryRepository userQueryRepository;

    @Override
    public List<User> search(String keyword, CursorPageable<Long> cPage) {
        return userQueryRepository.findAllByKeyword(keyword, cPage);
    }
}
