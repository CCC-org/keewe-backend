ALTER TABLE `insight`
    ADD COLUMN `view`              BIGINT          NOT NULL       DEFAULT 0;

ALTER TABLE `reaction`
    ADD COLUMN    created_at       DATETIME(6)     NOT NULL,
    ADD COLUMN    updated_at       DATETIME(6)     NOT NULL;

ALTER TABLE `reaction_aggregation`
    ADD COLUMN    created_at       DATETIME(6)     NOT NULL,
    ADD COLUMN    updated_at       DATETIME(6)     NOT NULL;