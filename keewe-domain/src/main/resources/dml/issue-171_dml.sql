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