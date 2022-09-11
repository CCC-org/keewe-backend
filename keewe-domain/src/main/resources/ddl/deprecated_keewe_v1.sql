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
    FOREIGN KEY (writer_id) REFERENCES `user`(user_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `post_like`
(
    user_id     BIGINT NOT NULL,
    post_id     BIGINT NOT NULL,
    created_at  DATETIME(6)     NOT NULL,
    updated_at  DATETIME(6)     NOT NULL,

    PRIMARY KEY (user_id, post_id),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    FOREIGN KEY (post_id) REFERENCES `post`(post_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE IF NOT EXISTS `candidate`
(
    candidate_id        BIGINT          NOT NULL    AUTO_INCREMENT,
    content             VARCHAR(25)     NOT NULL,
    post_id             BIGINT          NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (candidate_id),
    FOREIGN KEY (post_id) REFERENCES `post`(post_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `choice`
(
    candidate_id        BIGINT      NOT NULL,
    user_id             BIGINT      NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (candidate_id, user_id),
    FOREIGN KEY (candidate_id) REFERENCES `candidate`(candidate_id),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;