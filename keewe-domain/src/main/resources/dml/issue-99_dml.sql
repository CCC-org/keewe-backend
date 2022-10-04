ALTER TABLE `reaction`
    ADD COLUMN `reaction_id`    BIGINT  NOT NULL;

ALTER TABLE `reaction`
DROP PRIMARY KEY;

ALTER TABLE `reaction`
    ADD CONSTRAINT PRIMARY KEY (`reaction_id`);

ALTER TABLE `reaction`
    MODIFY COLUMN `reaction_id` BIGINT  NOT NULL AUTO_INCREMENT;