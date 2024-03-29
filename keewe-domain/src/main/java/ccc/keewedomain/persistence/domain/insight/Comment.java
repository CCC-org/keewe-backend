package ccc.keewedomain.persistence.domain.insight;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "insight_id", nullable = false)
    private Insight insight;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id", nullable = true)
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = LAZY)
    private List<Comment> replies = new ArrayList<>();

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public static Comment of(Insight insight, User writer, Comment parent, String content) {
        Comment comment = new Comment();
        comment.insight = insight;
        comment.writer = writer;
        comment.parent = parent;
        comment.content = content;

        return comment;
    }

    public void delete() {
        deleted = true;
    }

    public String getContent() {
        if (deleted) {
            if(Objects.isNull(parent)) {
                return "삭제된 댓글이에요.";
            }
            else {
                return "삭제된 답글이에요.";
            }
        }

        return content;
    }
}
