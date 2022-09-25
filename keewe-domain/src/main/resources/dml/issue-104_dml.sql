ALTER TABLE `insight`
    ADD COLUMN `view` BIGINT DEFAULT 0;

ALTER TABLE `reaction`
    ADD COLUMN    created_at       DATETIME(6)     NOT NULL,
    ADD COLUMN    updated_at       DATETIME(6)     NOT NULL,

ALTER TABLE `reaction_aggreation`
    ADD COLUMN    created_at       DATETIME(6)     NOT NULL,
    ADD COLUMN    updated_at       DATETIME(6)     NOT NULL,