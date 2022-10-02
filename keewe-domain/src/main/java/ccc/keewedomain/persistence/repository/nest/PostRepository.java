package ccc.keewedomain.persistence.repository.nest;

import ccc.keewedomain.persistence.domain.nest.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository <T extends Post> extends JpaRepository<T, Long> {
}
