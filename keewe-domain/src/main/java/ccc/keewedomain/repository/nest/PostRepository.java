package ccc.keewedomain.repository.nest;

import ccc.keewedomain.domain.nest.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository <T extends Post> extends JpaRepository<T, Long> {
}
