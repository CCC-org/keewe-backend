ALTER TABLE `user`
    ADD COLUMN rep_title_id BIGINT,
    ADD FOREIGN KEY (rep_title_id) REFERENCES `title`(title_id);

ALTER TABLE keewe.`comment`
    ADD CONSTRAINT FOREIGN KEY (parent_comment_id) REFERENCES `comment`(comment_id);