package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.insight.Bookmark;
import ccc.keewedomain.persistence.domain.insight.id.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
}
