CREATE TABLE IF NOT EXISTS `user_token` (
    user_id BIGINT NOT NULL,
    access_token VARCHAR(100),
    refresh_token VARCHAR(100),
    push_token VARCHAR(100),

    PRIMARY KEY (user_id)
)