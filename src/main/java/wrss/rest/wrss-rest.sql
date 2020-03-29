DROP DATABASE  IF EXISTS `rest-api`;

CREATE DATABASE  IF NOT EXISTS `rest-api`;
USE `rest-api`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` char(50) NOT NULL,
  `username` char(50) NOT NULL,
  `first_name` char(50) NOT NULL,
  `last_name` char(50) NOT NULL,
  `email` char(120) NOT NULL,
  `encrypted_password` char(120) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `user_details`;

CREATE TABLE `user_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id_fk` int(11) DEFAULT NULL,
  `city` char(50) NOT NULL,
  `street` char(50) NOT NULL,
  `phone_number` char(50) NOT NULL,
  PRIMARY KEY (`id`),
  
  KEY `FK_DETAILS_idx` (`user_id_fk`),
  CONSTRAINT `FK_DETAILS` FOREIGN KEY (`user_id_fk`) 
  REFERENCES `user` (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `post`;

CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id_fk` int(11) DEFAULT NULL,
  `title` char(50) NOT NULL,
  `message` char(255) NOT NULL,
  PRIMARY KEY (`id`),
  
  KEY `FK_POST_idx` (`user_id_fk`),
  CONSTRAINT `FK_POST` FOREIGN KEY (`user_id_fk`) 
  REFERENCES `user` (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
