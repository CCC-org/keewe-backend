package ccc.keewedomain.service.insight.query;

import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.id.BookmarkId;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.BookmarkQueryRepository;
import ccc.keewedomain.persistence.repository.insight.BookmarkRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookmarkQueryDomainService {
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkQueryRepository bookmarkQueryRepository;

    public boolean isBookmark(BookmarkId bookmarkId) {
        return bookmarkRepository.existsById(bookmarkId);
    }

    public Map<Long, Boolean> getBookmarkPresenceMap(User user, List<Insight> insightList) {
        return bookmarkQueryRepository.getBookmarkPresenceMap(user, insightList);
    }
}
