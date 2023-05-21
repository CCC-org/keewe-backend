CREATE TABLE IF NOT EXISTS `follow_from_insight`
(
    follower_id BIGINT NOT NULL,
    followee_id BIGINT NOT NULL,
    insight_id  BIGINT NOT NULL,

    PRIMARY KEY (insight_id, follower_id, followee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
