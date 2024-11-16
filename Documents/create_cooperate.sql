DROP TABLE IF EXISTS Tag;
CREATE TABLE Tag
(
	TagID INT NOT NULL AUTO_INCREMENT,
	Title VARCHAR(512) NOT NULL,
  Status INT, -- 1: active, 0: disable
	PRIMARY KEY(TagID)
)DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

DROP TABLE IF EXISTS Cooperate;
CREATE TABLE Cooperate
(
	CooperateID INT NOT NULL AUTO_INCREMENT,
	Title VARCHAR(512) NOT NULL,
	Brief VARCHAR(1024) NOT NULL,
	Price FLOAT,
	PriceString VARCHAR(125),
	Area VARCHAR(50),
	Detail TEXT,
	Thumb VARCHAR(255),
	Address VARCHAR(255),
	Longitude FLOAT,
  Latitude FLOAT,
  ContactName VARCHAR(125),
  ContactPhone VARCHAR(125),
  ContactAddress VARCHAR(512),
  ContactEmail VARCHAR(50),
  ExpireDate TIMESTAMP,
	PostDate TIMESTAMP NOT NULL,
  CityID INT,
  DistrictID INT,
  WardID INT,
  TagID INT,
  Street VARCHAR(255),
  Status INT, -- 1: active, 0: disable
  View INT,
  ModifiedDate TIMESTAMP,
  CreatedByID INT,
  UnitID INT,


	PRIMARY KEY(CooperateID),
	FOREIGN KEY(CityID) REFERENCES City(CityID) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(DistrictID) REFERENCES District(DistrictID) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(WardID) REFERENCES Ward(WardID) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(TagID) REFERENCES Tag(TagID) ON UPDATE CASCADE ON DELETE RESTRICT
)DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;


alter table cooperate drop column brief;
alter table cooperate drop column ExpireDate;
alter table cooperate drop column View;
alter table cooperate drop column Thumb;
alter table cooperate add column BrokerID INT REFERENCES us3r(Us3rID);
alter table cooperate add column Attachment1 VARCHAR(255);
alter table cooperate add column Attachment2 VARCHAR(255);
alter table cooperate add column Attachment3 VARCHAR(255);
alter table cooperate add column Attachment4 VARCHAR(255);
alter table cooperate add column Attachment5 VARCHAR(255);
alter table cooperate add column Demand VARCHAR(255) NOT NULL ;