DROP TABLE IF EXISTS CallMeBack;
CREATE TABLE CallMeBack
(
  CallMeBackID INT NOT NULL AUTO_INCREMENT,
  ProductID INT NOT NULL REFERENCES Product(ProductID),
  FullName VARCHAR(512),
  PhoneNumber VARCHAR(20) NOT NULL,
  Message TEXT,
  Status VARCHAR(20),
  CreatedDate TIMESTAMP NOT NULL,
  UpdatedDate TIMESTAMP NOT NULL,

  PRIMARY KEY(CallMeBackID)
)DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
alter table CallMeBack add CONSTRAINT UNIQUE (ProductID, PhoneNumber);
