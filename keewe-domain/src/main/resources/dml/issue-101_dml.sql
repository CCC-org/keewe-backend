ALTER TABLE `user`
    ADD COLUMN        vendor_id    VARCHAR(50)      NOT NULL UNIQUE,
    ADD COLUMN        vendor_type  VARCHAR(20)      NOT NULL,
    MODIFY COLUMN     email        VARCHAR(255);