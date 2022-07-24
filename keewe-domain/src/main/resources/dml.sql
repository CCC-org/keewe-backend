
INSERT INTO `user` (user_id, email, password, phone_number, status, deleted, created_at, updated_at)
VALUES(0, 'test@keewe.com', 'pass', '01012341234', 'ONBOARDING', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO `nest` (nest_id, deleted, created_at, updated_at)
VALUES (0, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO `profile` (profile_id, user_id, nickname, link, privacy, profile_photo_id, profile_status, deleted, created_at, updated_at, nest_id)
VALUES (0, 0,'', '', 'PUBLIC', null, 'LINK_NEEDED', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);