package ccc.keewedomain.repository;

import ccc.keewedomain.KeeweDomainApplication;
import ccc.keewedomain.dto.insight.CommentCreateDto;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.repository.insight.CommentQueryRepository;
import ccc.keewedomain.persistence.repository.insight.CommentRepository;
import ccc.keewedomain.service.insight.CommentDomainService;
import ccc.keeweinfra.KeeweInfraApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = {KeeweDomainApplication.class, KeeweInfraApplication.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-domain.yml"})
@ActiveProfiles("local")
public class CommentQueryRepositoryTest {

    @Autowired
    CommentQueryRepository commentQueryRepository;

    @Autowired
    CommentDomainService commentDomainService;

    @Autowired
    CommentRepository commentRepository;

    @Test
    @Transactional(readOnly = true)
    void test1() {
        List<Comment> comments = commentQueryRepository.findByReplyNumberDescWithUser(5L, 3L);
        System.out.println("comments.isEmpty() = " + comments.isEmpty());
        comments.forEach(
                comment -> {
                    System.out.println("id = " + comment.getId() + " content = " + comment.getContent());
                    System.out.println(comment.getReplies().size());
                }
        );
    }
}
