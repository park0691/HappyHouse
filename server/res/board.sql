CREATE TABLE board (
  BOARD_ID int NOT NULL AUTO_INCREMENT,
  USER_SEQ int NOT NULL,
  TITLE varchar(500) DEFAULT NULL,
  CONTENT text,
  REG_DT datetime DEFAULT NULL,
  READ_COUNT int DEFAULT '0',
  PRIMARY KEY (BOARD_ID),
  KEY FK_USER_idx (USER_SEQ),
  CONSTRAINT FK_USER FOREIGN KEY (USER_SEQ) REFERENCES user (USER_SEQ)
);

CREATE TABLE board_file (
  FILE_ID int NOT NULL AUTO_INCREMENT,
  BOARD_ID int NOT NULL,
  FILE_NAME varchar(500) NOT NULL,
  FILE_SIZE int NOT NULL,
  FILE_CONTENT_TYPE varchar(500) NOT NULL,
  FILE_URL varchar(500) NOT NULL,
  REG_DT datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (FILE_ID),
  KEY BOARD_FILE_FK_idx (BOARD_ID),
  CONSTRAINT BOARD_FILE_FK FOREIGN KEY (BOARD_ID) REFERENCES board (BOARD_ID)
);

CREATE TABLE board_user_read (
  BOARD_ID int NOT NULL,
  USER_SEQ int NOT NULL,
  PRIMARY KEY (BOARD_ID, USER_SEQ)
);

CREATE TABLE notice (
  NOTICE_ID int NOT NULL AUTO_INCREMENT,
  USER_SEQ int NOT NULL,
  TITLE varchar(500) DEFAULT NULL,
  CONTENT text,
  REG_DT datetime DEFAULT NULL,
  READ_COUNT int DEFAULT '0',
  PRIMARY KEY (NOTICE_ID),
  KEY FK_ADMIN_idx (USER_SEQ),
  CONSTRAINT FK_ADMIN FOREIGN KEY (USER_SEQ) REFERENCES user (USER_SEQ)
);

CREATE TABLE notice_file (
  FILE_ID int NOT NULL AUTO_INCREMENT,
  NOTICE_ID int NOT NULL,
  FILE_NAME varchar(500) NOT NULL,
  FILE_SIZE int NOT NULL,
  FILE_CONTENT_TYPE varchar(500) NOT NULL,
  FILE_URL varchar(500) NOT NULL,
  REG_DT datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (FILE_ID),
  KEY NOTICE_FILE_FK_idx (NOTICE_ID),
  CONSTRAINT NOTICE_FILE_FK FOREIGN KEY (NOTICE_ID) REFERENCES notice (NOTICE_ID)
);

CREATE TABLE notice_user_read (
  NOTICE_ID int NOT NULL,
  USER_SEQ int NOT NULL,
  PRIMARY KEY (NOTICE_ID, USER_SEQ)
);