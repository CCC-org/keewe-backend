CREATE TABLE IF NOT EXISTS `profile_visit_from_insight`
(
    insight_id  BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    PRIMARY KEY (insight_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
