CREATE TABLE IF NOT EXISTS `user`
(
    user_id             BIGINT(20)      AUTO_INCREMENT,
    email               VARCHAR(255),
    password            VARCHAR(255),
    phone_number        VARCHAR(255)    UNIQUE,
    status              VARCHAR(255),
    deleted             BIT             NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `profile_photo`
(
    profile_photo_id    BIGINT(20)      NOT NULL        AUTO_INCREMENT,
    deleted             BIT             NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (profile_photo_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `profile`
(
    profile_id          BIGINT(20)      NOT NULL        AUTO_INCREMENT,
    user_id             BIGINT(20)      NOT NULL,
    nickname            VARCHAR(30),
    link                VARCHAR(255)    UNIQUE,
    privacy             VARCHAR(20)     NOT NULL,
    profile_photo_id    BIGINT(20),
    nest_id             BIGINT          NOT NULL,
    profile_status      VARCHAR(30)     NOT NULL,
    deleted             BIT             NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (profile_id),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    FOREIGN KEY (profile_photo_id) REFERENCES  `profile_photo`(profile_photo_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `buddy`
(
    buddy_id            BIGINT(20)      NOT NULL        AUTO_INCREMENT,
    follower_id         BIGINT(20)      NOT NULL,
    followee_id         BIGINT(20)      NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (buddy_id),
    FOREIGN KEY (followee_id) REFERENCES `profile`(profile_id),
    FOREIGN KEY (follower_id) REFERENCES `profile`(profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `favorite_activities`
(
    profile_id      BIGINT(20)      NOT NULL,
    activity        VARCHAR(255)    NOT NULL,

    FOREIGN KEY (profile_id) REFERENCES `profile`(profile_id),
    CONSTRAINT `favorite_activities_constraint` UNIQUE (profile_id, activity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `social_link`
(
    social_link_id     BIGINT(20)       NOT NULL    AUTO_INCREMENT,
    profile_id          BIGINT(20)      NOT NULL,
    url                 VARCHAR(255)    NOT NULL,
    type                VARCHAR(255)    NOT NULL,
    deleted             BIT             NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (social_link_id),
    FOREIGN KEY (profile_id) REFERENCES `profile`(profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `nest`
(
    nest_id     BIGINT(20)      NOT NULL    AUTO_INCREMENT,
    deleted     BIT             NOT NULL,
    created_at  DATETIME(6)     NOT NULL,
    updated_at  DATETIME(6)     NOT NULL,

    PRIMARY KEY (nest_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `post`
(
    post_id     BIGINT(20)      NOT NULL    AUTO_INCREMENT,
    nest_id     BIGINT(20)      NOT NULL,
    writer_id   BIGINT          NOT NULL,
    like_count  BIGINT          NOT NULL,
    content     VARCHAR(140)    NOT NULL,
    post_type   VARCHAR(20)     NOT NULL,
    deleted     BIT             NOT NULL,
    created_at  DATETIME(6)     NOT NULL,
    updated_at  DATETIME(6)     NOT NULL,

    PRIMARY KEY (post_id),
    FOREIGN KEY (nest_id) REFERENCES `nest`(nest_id),
    FOREIGN KEY (writer_id) REFERENCES `profile`(profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `post_like`
(
    profile_id BIGINT NOT NULL,
    post_id    BIGINT NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (profile_id, post_id),
    FOREIGN KEY (profile_id) REFERENCES `profile`(profile_id),
    FOREIGN KEY (post_id) REFERENCES `post`(post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `comment`
(
    comment_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    writer_id BIGINT NOT NULL,
    parent_comment_id BIGINT,
    content VARCHAR(140) NOT NULL,
    deleted             BIT             NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (comment_id),
    FOREIGN KEY (post_id) REFERENCES `post`(post_id),
    FOREIGN KEY (writer_id) REFERENCES `profile`(profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `comment_like`
(
    comment_id          BIGINT          NOT NULL,
    profile_id          BIGINT          NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (comment_id, profile_id),
    FOREIGN KEY (comment_id) REFERENCES `comment`(comment_id),
    FOREIGN KEY (profile_id) REFERENCES `profile`(profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `candidate`
(
    candidate_id        BIGINT          NOT NULL    AUTO_INCREMENT,
    contents            VARCHAR(25)     NOT NULL,
    post_id             BIGINT          NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (candidate_id),
    FOREIGN KEY (post_id) REFERENCES `post`(post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `choice`
(
    candidate_id        BIGINT      NOT NULL,
    profile_id          BIGINT      NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (candidate_id, profile_id),
    FOREIGN KEY (candidate_id) REFERENCES `candidate`(candidate_id),
    FOREIGN KEY (profile_id) REFERENCES `profile`(profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;