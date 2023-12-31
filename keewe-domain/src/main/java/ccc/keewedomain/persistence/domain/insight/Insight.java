package ccc.keewedomain.persistence.domain.insight;

import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.common.Link;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "insight")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Insight extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "insight_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "challenge_participation_id")
    private ChallengeParticipation challengeParticipation;

    @Column(name = "contents", nullable = false, length = 300)
    private String contents;

    @Embedded
    private Link link;

    @Column(name = "valid", nullable = false)
    private boolean valid = false;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "insight", fetch = LAZY, orphanRemoval = true)
    private List<Reaction> reactions = new ArrayList<>();

    @OneToMany(mappedBy = "insight", fetch = LAZY, orphanRemoval = true)
    private List<ReactionAggregation> reactionAggregations = new ArrayList<>();

    @OneToMany(mappedBy = "insight", fetch = LAZY)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "drawer_id")
    private Drawer drawer;

    @Column(name = "view")
    private Long view = 0L;

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public ChallengeParticipation getChallengeParticipation() {
        return challengeParticipation;
    }

    public String getContents() {
        return contents;
    }

    public Link getLink() {
        return link;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public List<ReactionAggregation> getReactionAggregations() {
        return reactionAggregations;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public Long getView() {
        return view;
    }

    public static Insight of(User writer, ChallengeParticipation participation, Drawer drawer, String contents, Link link, boolean valid) {
        Insight insight = new Insight();
        insight.writer = writer;
        insight.challengeParticipation = participation;
        insight.drawer = drawer;
        insight.contents = contents;
        insight.link = link;
        insight.valid = valid;

        return insight;
    }

    public Long incrementView() {
        return ++view;
    }

    public void delete() {
        deleted = true;
    }

    public void removeDrawer() {
        this.drawer = null;
    }

    public void update(String contents, Link link, Drawer drawer) {
        this.contents = contents;
        this.link = link;
        this.drawer = drawer;
        if (drawer != null) {
            drawer.getInsights().add(this);
        }
    }
}
