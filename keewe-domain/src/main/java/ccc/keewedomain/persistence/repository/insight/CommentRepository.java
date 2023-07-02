package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndInsightId(Long id, Long insightId);
    List<Comment> findAllByWriter(User writer);
}
