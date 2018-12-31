CREATE TABLE  user (
    user_id BIGINT,
   user_name char(32) NOT NULL,
    user_pass char(32) NOT NULL,
    alliance_name char(32),
    doubloons int,
    PRIMARY KEY(user_id),
    UNIQUE KEY (user_name)
);

use bftg_db

ALTER TABLE user MODIFY user_id BIGINT NOT NULL AUTO_INCREMENT;

CREATE TABLE alliance (
	alliance_id BIGINT NOT NULL AUTO_INCREMENT,
    alliance_name char(32) NOT NULL,
    Primary Key(alliance_id),
    Unique key(alliance_name)
);

CREATE TABLE alliance_member (
    alliance_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    KEY(user_id),
    KEY(alliance_id),
    FOREIGN KEY(user_id) REFERENCES user(user_id) ON DELETE CASCADE
    FOREIGN KEY(alliance_id) REFERENCES alliance(alliance_id) ON DELETE CASCADE
    PRIMARY KEY(user_id, alliance_id)
);

INSERT INTO alliance (alliance_name)
VALUES("Club Chub");

#Add member to alliance
INSERT INTO alliance_member(alliance_id, user_id)
VALUES (1, 1);

#User alliance query
Select a.alliance_name
from alliance a, user u, alliance_member m
where u.user_id = m.user_id and a.alliance_id = m.alliance_id;