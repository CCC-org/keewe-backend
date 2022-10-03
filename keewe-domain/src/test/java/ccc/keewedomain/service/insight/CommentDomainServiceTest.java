package ccc.keewedomain.service.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.KeeweDomainApplication;
import ccc.keewedomain.dto.insight.CommentCreateDto;
import ccc.keewedomain.dto.user.UserSignUpDto;
import ccc.keewedomain.persistence.domain.common.Link;
import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.insight.Insight;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import ccc.keewedomain.persistence.repository.insight.CommentRepository;
import ccc.keewedomain.persistence.repository.insight.InsightRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.utils.DatabaseCleaner;
import ccc.keeweinfra.KeeweInfraApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = {KeeweDomainApplication.class, KeeweInfraApplication.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-domain.yml"})
@ActiveProfiles("test")
public class CommentDomainServiceTest {

    @Autowired
    CommentDomainService commentDomainService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InsightRepository insightRepository;

    @Autowired
    DatabaseCleaner databaseCleaner;

    User user = User.from(UserSignUpDto.of("vendorId", VendorType.NAVER, "boseong844@naver.com", null, null));;
    Insight insight = Insight.of(user, null, null, "인사이트 내용", Link.of("https://naver.com"));;
    Comment comment = Comment.of(insight, user, null, "댓글 내용");;

    @BeforeEach
    void setup() {
        userRepository.save(user);
        insightRepository.save(insight);
        commentRepository.save(comment);
    }

    @AfterEach
    void clean() {
        databaseCleaner.execute();
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void create_comment() {
        //given
        CommentCreateDto commentCreateDto = CommentCreateDto.of(user.getId(), insight.getId(), null, "내용");

        //when
        Comment createdComment = commentDomainService.create(commentCreateDto);
        Comment savedComment = commentRepository.findById(createdComment.getId()).get();

        //then
        assertAll(
                () -> assertThat(createdComment.getContent()).isEqualTo(savedComment.getContent()),
                () -> assertThat(createdComment.getId()).isEqualTo(savedComment.getId()),
                () -> assertThat(createdComment.getParent()).isEqualTo(savedComment.getParent()).isEqualTo(null),
                () -> assertThat(createdComment.getWriter().getId()).isEqualTo(savedComment.getWriter().getId())
        );
    }

    @Test
    @DisplayName("답글 생성 성공")
    void create_reply_comment() {
        //given
        CommentCreateDto replyCreateDto = CommentCreateDto.of(user.getId(), insight.getId(), comment.getId(), "내용");

        //when
        Comment reply = commentDomainService.create(replyCreateDto);
        Comment savedReply = commentRepository.findById(reply.getId()).get();

        //then
        assertAll(
                () -> assertThat(reply.getContent()).isEqualTo(savedReply.getContent()),
                () -> assertThat(reply.getId()).isEqualTo(savedReply.getId()),
                () -> assertThat(reply.getParent().getId()).isEqualTo(savedReply.getParent().getId()).isEqualTo(comment.getId()),
                () -> assertThat(reply.getWriter().getId()).isEqualTo(savedReply.getWriter().getId())
        );
    }

    @Test
    @DisplayName("답글에 답글 작성시 실패")
    void create_reply_on_reply() {
        //given
        Comment reply1 =  commentRepository.save(Comment.of(insight, user, comment, "답글 1번"));
        CommentCreateDto replyDto = CommentCreateDto.of(user.getId(), insight.getId(), reply1.getId(), "답글에 답글 달기");

        //when, then
        assertThatThrownBy(() -> commentDomainService.create(replyDto))
                .isExactlyInstanceOf(KeeweException.class)
                .hasMessage(KeeweRtnConsts.ERR443.getDescription());
    }

    @Test
    @DisplayName("존재하지 않는 인사이트에 댓글 작성 실패")
    void create_comment_on_invalid_insight() {
        //given
        Long invalidInsightId = 231232132L;
        CommentCreateDto commentCreateDto = CommentCreateDto.of(user.getId(), invalidInsightId, null, "내용");

        //when
        assertThatThrownBy(() -> commentDomainService.create(commentCreateDto))
                .isExactlyInstanceOf(KeeweException.class)
                .hasMessage(KeeweRtnConsts.ERR445.getDescription());
    }
}
