package ccc.keewedomain.persistence.domain.user;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.id.BookmarkId;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "bookmark")
@Getter
@IdClass(BookmarkId.class)
public class Bookmark extends BaseTimeEntity {
    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "insight_id")
    private Insight insight;
}
