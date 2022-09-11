CREATE TABLE IF NOT EXISTS `insight`
(
    insight_id                  BIGINT          NOT NULL    AUTO_INCREMENT,
    writer_id                   BIGINT          NOT NULL,
    challenge_participation_id  BIGINT          NOT NULL,
    contents                    VARCHAR(300)    NOT NULL,
    url                         VARCHAR(256)    NOT NULL, # FIXME size
    deleted                     BIT             NOT NULL,
    created_at                  DATETIME(6)     NOT NULL,
    updated_at                  DATETIME(6)     NOT NULL,

    PRIMARY KEY (insight_id),
    FOREIGN KEY (writer_id) REFERENCES `user`(user_id),
    FOREIGN KEY (challenge_participation_id) REFERENCES `challenge_participation`(challenge_participation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS 'reaction'
(
    insight_id    BIGINT      NOT NULL,
    reactor_id    BIGINT      NOT NULL,
    reaction_type VARCHAR(15) NOT NULL,

    PRIMARY KEY (insight_id, reactor_id, reaction_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS 'reaction_aggregation'
(
    insight_id             BIGINT      NOT NULL ,
    reaction_type          VARCHAR(15) NOT NULL,
    count                  BIGINT      NOT NULL  DEFAULT 0,

    PRIMARY KEY (insight_id, reaction_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;