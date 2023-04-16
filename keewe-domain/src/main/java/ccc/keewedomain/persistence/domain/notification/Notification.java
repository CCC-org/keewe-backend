package ccc.keewedomain.persistence.domain.notification;

import static javax.persistence.FetchType.LAZY;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.domain.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "notification")
@Getter
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "contents")
    @Enumerated(EnumType.STRING)
    private NotificationContents contents;

    // note. 챌린지 -> 챌린지참여 ID, 타이틀 -> 타이틀 ID, 댓글,답글 -> 인사이트 ID, 리액션 -> 인사이트 ID
    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "is_read")
    private boolean isRead = false;

    public static Notification of(User user, NotificationContents contents, String referenceId) {
        Notification notification = new Notification();
        notification.user = user;
        notification.contents = contents;
        notification.referenceId = referenceId;
        return notification;
    }

    public Notification markAsRead() {
        this.isRead = true;
        return this;
    }
}
