CREATE TABLE IF NOT EXISTS `bookmark` (
     user_id             BIGINT          NOT NULL,
     insight_id             BIGINT          NOT NULL,
     PRIMARY KEY(user_id, insight_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;