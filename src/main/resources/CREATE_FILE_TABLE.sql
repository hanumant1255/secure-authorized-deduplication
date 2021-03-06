USE SAKTHI;
DROP TABLE IF EXISTS FILE;
CREATE TABLE FILE(
ID INT NOT NULL AUTO_INCREMENT,
NAME  VARCHAR(500),
FILE_KEY VARCHAR(500),
URL VARCHAR(500),
ROLE  VARCHAR(100),
USER_ID INT,
PRIMARY KEY(ID),
FOREIGN KEY (USER_ID) REFERENCES USER(ID)
);