ALTER TABLE `user`
    ADD COLUMN rep_title_id BIGINT,
    ADD FOREIGN KEY (rep_title_id) REFERENCES `title`(title_id);