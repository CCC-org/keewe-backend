package ccc.keewedomain.persistence.domain.title;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.title.id.TitleAchievementId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "title_achievement")
@IdClass(TitleAchievementId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TitleAchievement extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "title_id")
    private Title title;

    public static TitleAchievement of(User user, Title title) {
        TitleAchievement titleAchievement = new TitleAchievement();
        titleAchievement.user = user;
        titleAchievement.title = title;
        return titleAchievement;
    }
}
