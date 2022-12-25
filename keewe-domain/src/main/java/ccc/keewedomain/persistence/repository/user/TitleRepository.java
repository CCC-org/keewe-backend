package ccc.keewedomain.persistence.repository.user;

import ccc.keewedomain.persistence.domain.title.Title;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository extends JpaRepository<Title, Long> {
}
