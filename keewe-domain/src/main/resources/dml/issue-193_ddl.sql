CREATE TABLE IF NOT EXISTS `report`
(
    report_id           BIGINT        NOT NULL     AUTO_INCREMENT,
    reporter_id         BIGINT         NOT NULL,
    insight_id          BIGINT         NOT NULL,
    report_type         VARCHAR(30)     NOT NULL,
    reason              VARCHAR(2000),
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,

    PRIMARY KEY(report_id),
    FOREIGN KEY (reporter_id) REFERENCES  `user`(user_id),
    FOREIGN KEY (insight_id) REFERENCES `insight`(insight_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
