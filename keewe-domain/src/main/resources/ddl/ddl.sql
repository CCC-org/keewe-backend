/**
  유저 관련 테이블 정의
 */
CREATE TABLE IF NOT EXISTS `profile_photo`
(
    profile_photo_id    BIGINT(20)      NOT NULL        AUTO_INCREMENT,
    image               VARCHAR(255)    NOT NULL,
    deleted             BIT             NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (profile_photo_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user`
(
    user_id             BIGINT          NOT NULL     AUTO_INCREMENT,
    email               VARCHAR(255),
    vendor_id           VARCHAR(50)     NOT NULL     UNIQUE,
    vendor_type         VARCHAR(20)     NOT NULL,
    password            VARCHAR(255),
    phone_number        VARCHAR(255)    UNIQUE,
    nickname            VARCHAR(12),
    profile_photo_id    BIGINT(20),
    privacy             VARCHAR(20)     NOT NULL,
    status              VARCHAR(255),
    rep_title_id        BIGINT,
    introduction        VARCHAR(150)    NOT NULL,
    deleted             BIT             NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY(user_id),
    FOREIGN KEY (profile_photo_id) REFERENCES  `profile_photo`(profile_photo_id),
    FOREIGN KEY (rep_title_id) REFERENCES `title`(title_id),
    CONSTRAINT `vendor_constraint` UNIQUE (`vendor_id`, `vendor_type`),
    INDEX  `vendor_index` (`vendor_id`, `vendor_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `follow`
(
    follower_id         BIGINT(20)      NOT NULL,
    followee_id         BIGINT(20)      NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (follower_id, followee_id),
    FOREIGN KEY (followee_id) REFERENCES `user`(user_id),
    FOREIGN KEY (follower_id) REFERENCES `user`(user_id),
    INDEX `created_at_index` (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `block`
(
    user_id         BIGINT(20)      NOT NULL,
    blocked_user_id BIGINT(20)      NOT NULL,
    created_at      DATETIME(6)     NOT NULL,
    updated_at      DATETIME(6)     NOT NULL,

    PRIMARY KEY (user_id, blocked_user_id),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    FOREIGN KEY (blocked_user_id) REFERENCES `user`(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `favorite_interests`
(
    user_id         BIGINT(20)      NOT NULL,
    interest_name   VARCHAR(255)    NOT NULL,

    FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    CONSTRAINT `favorite_activities_constraint` UNIQUE (user_id, interest_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `title`
(
    title_id        BIGINT          NOT NULL,
    category        VARCHAR(255)    NOT NULL,
    name            VARCHAR(255)    NOT NULL,
    introduction    VARCHAR(255)    NOT NULL,

    PRIMARY KEY (title_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `title_achievement`
(
    user_id         BIGINT          NOT NULL,
    title_id        BIGINT          NOT NULL,
    created_at      DATETIME(6)     NOT NULL,
    updated_at      DATETIME(6)     NOT NULL,

    PRIMARY KEY (user_id, title_id),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    FOREIGN KEY (title_id) REFERENCES `title`(title_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/**
  챌린지 관련 테이블 정의
 */
CREATE TABLE IF NOT EXISTS `challenge`
(
    challenge_id    BIGINT          NOT NULL    AUTO_INCREMENT,
    writer_id       BIGINT          NOT NULL,
    interest_name   VARCHAR(8)      NOT NULL,
    name            VARCHAR(25)     NOT NULL,
    introduction    VARCHAR(150)    NOT NULL,
    deleted         BIT             NOT NULL,
    created_at      DATETIME(6)     NOT NULL,
    updated_at      DATETIME(6)     NOT NULL,

    PRIMARY KEY (challenge_id),
    FOREIGN KEY (writer_id) REFERENCES `user`(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `challenge_participation`
(
    challenge_participation_id  BIGINT          NOT NULL    AUTO_INCREMENT,
    challenger_id               BIGINT          NOT NULL,
    challenge_id                BIGINT          NOT NULL,
    my_topic                    VARCHAR(25)     NOT NULL,
    insight_per_week            INT             NOT NULL,
    duration                    INT             NOT NULL,
    deleted                     BIT             NOT NULL,
    end_date                    DATE            NOT NULL,
    status                      VARCHAR(20)     NOT NULL,
    created_at                  DATETIME(6)     NOT NULL,
    updated_at                  DATETIME(6)     NOT NULL,

    PRIMARY KEY (challenge_participation_id),
    FOREIGN KEY (challenger_id) REFERENCES `user`(user_id),
    FOREIGN KEY (challenge_id) REFERENCES `challenge`(challenge_id),
    INDEX `CHALLENGER_STATUS_IDX`(challenger_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/**
 * 인사이트 (게시물) 관련 테이블 정의
 */

CREATE TABLE IF NOT EXISTS `drawer`
(
    drawer_id       BIGINT          NOT NULL    AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    name            VARCHAR(15)     NOT NULL,
    deleted         BIT             NOT NULL,
    created_at      DATETIME(6)     NOT NULL,
    updated_at      DATETIME(6)     NOT NULL,

    PRIMARY KEY (drawer_id),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    CONSTRAINT `drawer_name_constraint` UNIQUE (user_id, name)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `insight`
(
    insight_id                  BIGINT          NOT NULL    AUTO_INCREMENT,
    writer_id                   BIGINT          NOT NULL,
    drawer_id                   BIGINT,
    challenge_participation_id  BIGINT,
    contents                    VARCHAR(300)    NOT NULL,
    url                         VARCHAR(2000)   NOT NULL,
    deleted                     BIT             NOT NULL,
    valid                       BIT             NOT NULL,
    view                        BIGINT          NOT NULL    DEFAULT  0,
    created_at                  DATETIME(6)     NOT NULL,
    updated_at                  DATETIME(6)     NOT NULL,


    PRIMARY KEY (insight_id),
    FOREIGN KEY (writer_id) REFERENCES `user`(user_id),
    FOREIGN KEY (challenge_participation_id) REFERENCES `challenge_participation`(challenge_participation_id),
    FOREIGN KEY (drawer_id) REFERENCES `drawer`(drawer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `reaction`
(
    reaction_id         BIGINT      NOT NULL AUTO_INCREMENT,
    insight_id          BIGINT      NOT NULL,
    reactor_id          BIGINT      NOT NULL,
    reaction_type       VARCHAR(15) NOT NULL,
    created_at          DATETIME(6) NOT NULL,
    updated_at          DATETIME(6) NOT NULL,

    FOREIGN KEY (insight_id) REFERENCES `insight`(insight_id),
    FOREIGN KEY (reaction_id) REFERENCES `user`(user_id),
    PRIMARY KEY (reaction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `reaction_aggregation`
(
    insight_id          BIGINT          NOT NULL,
    reaction_type       VARCHAR(15)     NOT NULL,
    count               BIGINT          NOT NULL  DEFAULT 0,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    FOREIGN KEY (insight_id) REFERENCES `insight`(insight_id),
    PRIMARY KEY (insight_id, reaction_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/**
  인사이트 게시물 댓글 관련 테이블 정의
 */
CREATE TABLE IF NOT EXISTS `comment`
(
    comment_id          BIGINT(20)      NOT NULL    AUTO_INCREMENT,
    insight_id          BIGINT          NOT NULL,
    writer_id           BIGINT          NOT NULL,
    parent_comment_id   BIGINT,
    content             VARCHAR(140)    NOT NULL,
    deleted             BIT             NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (comment_id),
    FOREIGN KEY (insight_id) REFERENCES `insight`(insight_id),
    FOREIGN KEY (writer_id) REFERENCES `user`(user_id),
    FOREIGN KEY (parent_comment_id) REFERENCES `comment`(comment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `comment_like`
(
    comment_id          BIGINT          NOT NULL,
    user_id             BIGINT          NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (comment_id, user_id),
    FOREIGN KEY (comment_id) REFERENCES `comment`(comment_id),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE IF NOT EXISTS `bookmark` (
    user_id             BIGINT          NOT NULL,
    insight_id          BIGINT          NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    FOREIGN KEY (insight_id) REFERENCES `insight`(insight_id),
    PRIMARY KEY (user_id, insight_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE IF NOT EXISTS `report`
(
    report_id           BIGINT        NOT NULL     AUTO_INCREMENT,
    reporter_id         BIGINT         NOT NULL,
    insight_id          BIGINT         NOT NULL,
    report_type         VARCHAR(30)     NOT NULL,
    reason              VARCHAR(2000),
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (report_id),
    FOREIGN KEY (reporter_id) REFERENCES `user`(user_id),
    FOREIGN KEY (insight_id) REFERENCES `insight`(insight_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
