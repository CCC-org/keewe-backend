ALTER TABLE `user`
    ADD COLUMN        vendor_id    VARCHAR(50)      NOT NULL,
    ADD COLUMN        vendor_type  VARCHAR(20)      NOT NULL,
    MODIFY COLUMN     email        VARCHAR(255),
    ADD CONSTRAINT `vendor_constraint` UNIQUE (`vendor_id`, `vendor_type`),
    ADD INDEX  `vendor_index` (`vendor_id`, `vendor_type`);