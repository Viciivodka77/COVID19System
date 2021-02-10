CREATE TABLE `user`(
  `uID` INT(10) NOT NULL AUTO_INCREMENT ,
  `uName` VARCHAR(10) NOT NULL ,
  `uPassword` VARCHAR(20) NOT NULL ,
  `uStatus` INT(3) NOT NULL ,
  `address` VARCHAR(100) ,
  `birthday` BIGINT ,
  `createTime` BIGINT NOT NULL,
  `updateTime` BIGINT NOT NULL,
  PRIMARY KEY (`uID`)
)ENGINE= INNODB DEFAULT CHARSET=utf8

INSERT INTO `user` (uID,uName,uPassword,uStatus,createTime,updateTime)VALUES
(NULL,"zs",123456,0,1602506357947,1602506357947),
(NULL,"ls",123456,1,1602506357947,1602506357947);

SELECT * FROM USER

CREATE TABLE `user`(
  `uID` INT(10) NOT NULL AUTO_INCREMENT ,
  `uName` VARCHAR(10) NOT NULL ,
  `uPassword` VARCHAR(20) NOT NULL ,
  `uStatus` INT(3) NOT NULL ,
  `address` VARCHAR(100) ,
  `birthday` BIGINT ,
  `createTime` BIGINT NOT NULL,
  `updateTime` BIGINT NOT NULL,
  PRIMARY KEY (`uID`)
)ENGINE= INNODB DEFAULT CHARSET=utf8

INSERT INTO `user` (uID,uName,uPassword,uStatus,createTime,updateTime)VALUES
(NULL,"zs",123456,0,1602506357947,1602506357947),
(NULL,"ls",123456,1,1602506357947,1602506357947);

CREATE TABLE `role`(
  `rID` INT AUTO_INCREMENT NOT NULL,
  `level` INT(3) NOT NULL UNIQUE,
  PRIMARY KEY (`rID`)
)ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO role (rID,LEVEL) VALUES(NULL,1),(NULL,2),(NULL,3),(NULL,4)

SELECT * FROM role

CREATE TABLE `user_role`(
  `id` INT(15) AUTO_INCREMENT NOT NULL, 
  `uID` INT(10) NOT NULL,
  `rID` INT(5) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_role_to_role` (`rID`),
  KEY `user_role_to_user` (`uID`),
  CONSTRAINT `user_role_to_role` FOREIGN KEY (`rID`) REFERENCES `role` (`rID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_role_to_user` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8
,
INSERT INTO user_role (id,uID,rID) VALUES(NULL,1,1),(NULL,2,1),(NULL,2,2),(NULL,2,3),(NULL,2,4)

SELECT * FROM user_role WHERE uID = 2

CREATE TABLE `clock_in`(
  `id` INT(30) AUTO_INCREMENT NOT NULL,
  `uID` INT(10) NOT NULL,
  `temperature` FLOAT NOT NULL,
  `time` DATETIME NOT NULL,
  `bmStatus` INT(3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_clock_in_fk` (`uID`),
  CONSTRAINT `user_clock_in_fk` FOREIGN KEY (`uID`) REFERENCES `user` (`uID`) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8


CREATE TABLE `web_info`(
  `id` INT(30) AUTO_INCREMENT NOT NULL,
  `notice` VARCHAR(500) NOT NULL,
  `footer` VARCHAR(150) NOT NULL,
  `logo` VARCHAR(300) NOT NULL,
  `title` VARCHAR(50) NOT NULL,
  `update_time` DATETIME NOT NULL,
  `submitted_by` INT(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `submitted_by_in_fk` (`submitted_by`),
  CONSTRAINT `submitted_by_in_fk` FOREIGN KEY (`submitted_by`) REFERENCES `user` (`uID`) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO web_info VALUE (NULL,"本系统所有数据均只用于毕业设计,不做任何商业用途.数据来源丁香园","本系统所有数据均只用于毕业设计,不做任何商业用途.数据来源丁香园","E:\IDEAProject\GraduateDesign\src\main\resources\static\imgs\Hui.png","疫情一体化管理系统","2020-11-13 13:07:15",2);

SELECT * FROM `web_info`;


CREATE TABLE `product`(
  `id` INT(30) AUTO_INCREMENT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `details` VARCHAR(500) NOT NULL,
  `price` DECIMAL(12,2) NOT NULL,
  `stock` INT(30) NOT NULL,
  `created_time` DATE NOT NULL,
  `updated_time` DATE NOT NULL,
  `img_path` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8

SELECT * FROM product


CREATE TABLE `order`(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(10) NOT NULL,
  `order_no` BIGINT(20) NOT NULL UNIQUE,
  `order_detail` VARCHAR(100) DEFAULT NULL,
  `order_price` DECIMAL(15,2) NOT NULL,
  `order_status` INT(2) NOT NULL,
  `create_time` DATE NOT NULL,
  `update_time` DATE NOT NULL,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8


CREATE TABLE `ticket`(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `ticket_no` BIGINT(20) DEFAULT NULL UNIQUE,
  `parent_no` BIGINT(20) DEFAULT NULL ,
  `user_id` INT(11) NOT NULL,
  `user_name` VARCHAR(24) NOT NULL,
  `user_phone` BIGINT(12) NOT NULL,
  `subject` VARCHAR(24) DEFAULT NULL,
  `content` VARCHAR(500) NOT NULL,
  `department` INT(3) DEFAULT NULL,
  `status` INT(3) DEFAULT NULL,
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8

