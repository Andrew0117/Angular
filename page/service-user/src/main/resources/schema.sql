
drop table IF EXISTS osoba;

CREATE TABLE IF NOT EXISTS `osoba` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`e_mail` VARCHAR(100) NOT NULL,
	`home_phone` VARCHAR(100) NOT NULL,
	`name` VARCHAR(100) NOT NULL,
	`office_phone` VARCHAR(100) NOT NULL,
	`photo_of_a_person` LONGBLOB NULL DEFAULT NULL,
	`surname` VARCHAR(100) NOT NULL,
	constraint PK_osoba primary key (id)
);

