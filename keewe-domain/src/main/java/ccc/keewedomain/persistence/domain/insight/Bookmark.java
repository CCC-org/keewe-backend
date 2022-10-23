package ccc.keewedomain.persistence.domain.insight;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.insight.id.BookmarkId;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "bookmark")
@Getter
@IdClass(BookmarkId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTimeEntity {
    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "insight_id")
    private Insight insight;

    public static Bookmark of(User user, Insight insight) {
        Bookmark bookmark = new Bookmark();
        bookmark.user = user;
        bookmark.insight = insight;
        return bookmark;
    }
}
