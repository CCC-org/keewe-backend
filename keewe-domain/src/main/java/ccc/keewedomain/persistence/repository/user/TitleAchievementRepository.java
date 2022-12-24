package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.id.TitleAchievementId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleAchievementRepository extends JpaRepository<TitleAchievement, TitleAchievementId> {

}
