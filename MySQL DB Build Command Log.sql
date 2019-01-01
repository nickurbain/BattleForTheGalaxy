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

CREATE TABLE alliance_admiral (
    user_id BIGINT NOT NULL,
    alliance_id BIGINT NOT NULL,
    KEY(user_id),
    KEY(alliance_id),
    FOREIGN KEY(user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY(alliance_id) REFERENCES alliance(alliance_id) ON DELETE CASCADE,
    PRIMARY KEY(user_id, alliance_id)
);

Insert Into alliance_admiral values(user_id, alliance_id)
VALUES (1, 1);

SELECT m.alliance_id FROM Alliance_member m WHERE m.user_id = 2;

Create Table items (
    item_id int NOT NULL,
    item_type int,
    item_value int,
    item_weight int,
    item_power int,
    item_power_cost int,
    Primary Key(item_id)
);

Create Table inventory (
    user_id bigint NOT NULL,
    item_id bigint NOT NULL,
    Key(user_id),
    Key(item_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY(item_id) REFERENCES items(item_id) ON DELETE CASCADE,
    Primary Key(user_id, item_id)
);


Create Table equipped(
    user_id bigint NOT NULL,
    item_id bigint NOT NULL,
    slot int NOT NULL,
    Key(user_id),
    Key(item_id),
    FOREIGN KEY(user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY(item_id) REFERENCES items(item_id) ON DELETE CASCADE,
    Primary Key(user_id, item_id, slot)
);

Alter table items add column item_name char(32)
