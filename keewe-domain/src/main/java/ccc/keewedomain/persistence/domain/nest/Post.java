package ccc.keewedomain.persistence.domain.nest;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.nest.enums.PostType;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "post")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type")
@NoArgsConstructor
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "nest_id", nullable = false)
//    private Nest nest;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "content", nullable = false, length = 140)
    private String content;

    @OneToMany(mappedBy = "post", fetch = LAZY)
    private List<PostLike> likes = new ArrayList<>();

//    @OneToMany(mappedBy = "post", fetch = LAZY)
//    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", insertable = false, updatable = false)
    private PostType postType;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public static final long CONTENT_MAX_LENGTH = 140L;

    protected Post(User writer, String content) {
        this.writer = writer;
        this.content = content;
    }
}
