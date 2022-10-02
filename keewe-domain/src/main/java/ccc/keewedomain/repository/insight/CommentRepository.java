package ccc.keewedomain.repository.insight;

import ccc.keewedomain.domain.insight.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndInsightId(Long id, Long insightId);
}
