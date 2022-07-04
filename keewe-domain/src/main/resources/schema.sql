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
    nickname            VARCHAR(30)     NOT NULL,
    link                VARCHAR(30)     NOT NULL,
    privacy             VARCHAR(20)     NOT NULL,
    profile_photo_id    BIGINT(20),
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
    FOREIGN KEY (followee_id) REFERENCES `profile`(profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `favorite_activities`
(
    profile_id      BIGINT(20)      NOT NULL,
    activity        VARCHAR(255)    NOT NULL,
    created_at      DATETIME(6)     NOT NULL,
    updated_at      DATETIME(6)     NOT NULL,

    FOREIGN KEY (profile_id) REFERENCES `profile`(profile_id),
    CONSTRAINT `favorite_activities_constraint` UNIQUE (profile_id, activity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `profile_link`
(
    profile_link_id     BIGINT(20)      NOT NULL        AUTO_INCREMENT,
    profile_id          BIGINT(20)      NOT NULL,
    url                 VARCHAR(255)    NOT NULL,
    type                VARCHAR(255)    NOT NULL,
    deleted             BIT             NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (profile_link_id),
    FOREIGN KEY (profile_id) REFERENCES `profile`(profile_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

