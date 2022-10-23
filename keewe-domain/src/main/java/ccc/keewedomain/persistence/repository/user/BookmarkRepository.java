package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.user.Bookmark;
import ccc.keewedomain.persistence.domain.user.id.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
}
