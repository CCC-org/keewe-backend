package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.nest.enums.PostType;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type")
public abstract class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nest_id")
    private Nest nest;

    private Long likeCount;

    private String content;

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", insertable = false, updatable = false)
    private PostType postType;
}
