CREATE Table Person(
Id BIGINT NOT NULL,
Name varchar(255) default NULL,
Screenname varchar(255) default NULL,
Location varchar(255) default NULL,
Description varchar(255) default NULL,
URL varchar(255) default NULL,
Profile_image_url varchar(255) default NULL,
Followers_count INT default NULL,
Friends_count INT default NULL,
Created_at varchar(255) default NULL,
Extracted_at varchar(255) default NULL,
Time_zone varchar(255) default NULL,
Status_count INT default NULL,
Lang varchar(255) default NULL,
Listed_count INT default NULL,
FollowsPersonId1 BIGINT default NULL,
FollowsPersonId2 BIGINT default NULL,
primary key(Id)
);
CREATE Table Friend (
PersonId1 BIGINT NOT NULL,
PersonId2 BIGINT NOT NULL,
Extracted_at varchar(255) default NULL
);
CREATE Table Follows (
PersonId1 BIGINT NOT NULL,
PersonId2 BIGINT NOT NULL,
Extracted_at varchar(255) default NULL
);
CREATE Table Status(
Id BIGINT NOT NULL,
PersonId2 BIGINT NOT NULL,
Created_at varchar(255) default NULL,
Extracted_at varchar(255) default NULL,
Text varchar(140) default NULL,
Geo varchar(255) default NULL,
Coordinates varchar(255) default NULL,
Place varchar(255) default NULL,
Source INT default NULL,
primary key(Id)
);
CREATE Table Status_Hashtag (
StatusId BIGINT NOT NULL,
HashtagId BIGINT NOT NULL
);
CREATE Table Hashtag (
Id BIGINT NOT NULL,
Geo varchar(30) default NULL,
primary key(Id)
);
CREATE Table Status_URL (
StatusId BIGINT NOT NULL,
URLId BIGINT default NULL
);
CREATE Table URL (
Id BIGINT NOT NULL,
URL varchar(255) default NULL,
primary key(Id)
);
