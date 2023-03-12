package ccc.keewedomain.service.insight.command;

import ccc.keewedomain.dto.insight.BookmarkToggleDto;
import ccc.keewedomain.persistence.domain.insight.Bookmark;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.insight.id.BookmarkId;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.insight.BookmarkRepository;
import ccc.keewedomain.service.insight.query.BookmarkQueryDomainService;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookmarkCommandDomainService {

    private final UserDomainService userDomainService;
    private final InsightQueryDomainService insightQueryDomainService;
    private final BookmarkQueryDomainService bookmarkQueryDomainService;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public boolean toggleInsightBookmark(BookmarkToggleDto dto) {
        User user = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        Insight insight = insightQueryDomainService.getByIdOrElseThrow(dto.getInsightId());

        BookmarkId bookmarkId = BookmarkId.of(dto.getUserId(), dto.getInsightId());

        bookmarkRepository.findById(bookmarkId)
                .ifPresentOrElse(
                        bookmark -> {
                            log.info("[IDS::toggleInsightBookmark] Found Bookmark user {}, insight {}", bookmarkId.getUser(), bookmarkId.getInsight());
                            bookmarkRepository.delete(bookmark);
                        },
                        () -> {
                            log.info("[IDS::toggleInsightBookmark] Not Found Bookmark user {}, insight {}", bookmarkId.getUser(), bookmarkId.getInsight());
                            bookmarkRepository.save(Bookmark.of(user, insight));
                        }
                );
        return bookmarkQueryDomainService.isBookmark(bookmarkId);
    }
}
