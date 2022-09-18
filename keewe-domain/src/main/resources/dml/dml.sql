
INSERT INTO `user` (user_id, email, password, phone_number, status, deleted, created_at, updated_at, privacy, vendor_id, vendor_type)
VALUES(0, 'test@keewe.com', 'pass', '01012341234', 'ONBOARD', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'PUBLIC', '0000001', 'KAKAO');

INSERT INTO `challenge` (challenge_id, writer_id, interest_name, name, introduction, deleted, created_at, updated_at)
VALUES (0, 0, 'asdf', 'asdf', 'asdf', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP );

INSERT INTO `challenge_participation` (challenge_participation_id,
                                       challenger_id,
                                       challenge_id,
                                       my_topic,
                                       insight_per_week,
                                       duration,
                                       deleted,
                                       end_date,
                                       status,
                                       created_at,
                                       updated_at)
VALUES (0, 0, 0, 'asdf', 1, 1, false, CURRENT_TIMESTAMP , 'T', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO `insight` (insight_id, writer_id, challenge_participation_id, contents, url, deleted, created_at, updated_at)
    VALUES(0, 0, 0, 'asdf', 'asdf', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);