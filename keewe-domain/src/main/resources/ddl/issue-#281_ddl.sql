CREATE TABLE IF NOT EXISTS `notification`
(
    notification_id     BIGINT          NOT NULL     AUTO_INCREMENT,
    user_id             BIGINT          NOT NULL,
    contents            VARCHAR(30)     NOT NULL,
    reference_id        VARCHAR(30)     NOT NULL,
    is_read             boolean         NOT NULL default false,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY (notification_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
