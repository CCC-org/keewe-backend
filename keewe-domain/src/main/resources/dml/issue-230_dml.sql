ALTER TABLE `reaction`
    ADD CONSTRAINT FOREIGN KEY (insight_id) REFERENCES `insight`(insight_id),
    ADD CONSTRAINT FOREIGN KEY (reactor_id) REFERENCES `user`(user_id);

ALTER TABLE `reaction_aggregation`
    ADD CONSTRAINT FOREIGN KEY (insight_id) REFERENCES `insight`(insight_id);

ALTER TABLE `bookmark`
    ADD CONSTRAINT FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    ADD CONSTRAINT FOREIGN KEY (insight_id) REFERENCES `insight`(insight_id);