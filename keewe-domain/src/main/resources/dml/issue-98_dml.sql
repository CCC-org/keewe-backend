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

ALTER TABLE `insight`
    ADD COLUMN drawer_id BIGINT,
    ADD FOREIGN KEY (drawer_id) REFERENCES `drawer`(drawer_id),
    MODIFY COLUMN challenge_participation_id BIGINT;