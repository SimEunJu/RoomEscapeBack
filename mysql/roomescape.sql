-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: escape
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `board_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `content` longtext,
  `good` int DEFAULT '0',
  `report` int DEFAULT '0',
  `title` text,
  `view` int DEFAULT '0',
  `member_id` bigint NOT NULL,
  `btype` varchar(31) NOT NULL,
  PRIMARY KEY (`board_id`),
  KEY `FKsds8ox89wwf6aihinar49rmfy` (`member_id`),
  CONSTRAINT `FKsds8ox89wwf6aihinar49rmfy` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `ctype` varchar(31) NOT NULL,
  `comment_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `content` text,
  `depth` int DEFAULT '0',
  `good` int NOT NULL DEFAULT '0',
  `is_hidden` bit(1) NOT NULL,
  `par_id` bigint DEFAULT NULL,
  `refer_id` bigint DEFAULT NULL,
  `seq` int DEFAULT '0',
  `star` int NOT NULL DEFAULT '0',
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FKmrrrpi513ssu63i2783jyiv9m` (`member_id`),
  CONSTRAINT `FKmrrrpi513ssu63i2783jyiv9m` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `ftype` varchar(31) NOT NULL,
  `file_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `original_name` varchar(255) DEFAULT NULL,
  `refer_id` bigint DEFAULT NULL,
  `root_path` varchar(255) DEFAULT NULL,
  `seq` int DEFAULT NULL,
  `sub_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `good`
--

DROP TABLE IF EXISTS `good`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `good` (
  `gtype` varchar(31) NOT NULL,
  `good_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `is_good` bit(1) NOT NULL,
  `refer_id` bigint DEFAULT NULL,
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`good_id`),
  KEY `FK9nfyianeesymee8qm2x9gs2i2` (`member_id`),
  CONSTRAINT `FK9nfyianeesymee8qm2x9gs2i2` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `email` varchar(1000) NOT NULL,
  `leave_date` datetime DEFAULT NULL,
  `member_name` varchar(100) DEFAULT NULL,
  `nickname` varchar(200) DEFAULT NULL,
  `password` longtext,
  `social_login` varchar(100) NOT NULL,
  `unable` tinyint(1) DEFAULT '0',
  `unable_date` datetime DEFAULT NULL,
  `unable_reason` text,
  `is_withdrawal` bit(1) NOT NULL,
  `withdrawal_date` datetime DEFAULT NULL,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `UK_mbmcqelty0fbrvxp1q58dn57t` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `member_roles`
--

DROP TABLE IF EXISTS `member_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_roles` (
  `member_member_id` bigint NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  KEY `FKruptm2dtwl95mfks4bnhv828k` (`member_member_id`),
  CONSTRAINT `FKruptm2dtwl95mfks4bnhv828k` FOREIGN KEY (`member_member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store` (
  `store_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `addr` longtext,
  `addr_etc` longtext,
  `detail_addr` longtext,
  `postcode` smallint unsigned DEFAULT NULL,
  `area_code` double DEFAULT NULL,
  `introduce` text,
  `link` longtext,
  `location` geometry DEFAULT NULL,
  `tel` varchar(100) DEFAULT NULL,
  `theme_cnt` smallint DEFAULT '0',
  `name` longtext,
  PRIMARY KEY (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `theme`
--

DROP TABLE IF EXISTS `theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `theme` (
  `theme_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `difficulty` tinyint unsigned DEFAULT NULL,
  `genre` text,
  `introduce` text,
  `link` longtext,
  `minutes` smallint unsigned DEFAULT NULL,
  `personnel` tinyint unsigned DEFAULT NULL,
  `quiz_type` text,
  `store_id` bigint NOT NULL,
  `name` longtext,
  PRIMARY KEY (`theme_id`),
  KEY `FKcrj39cbn6jldet3ekokpnb31` (`store_id`),
  CONSTRAINT `FKcrj39cbn6jldet3ekokpnb31` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `theme_comment`
--

DROP TABLE IF EXISTS `theme_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `theme_comment` (
  `theme_comment_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `diffculty` int NOT NULL DEFAULT '0',
  `flower_road` int DEFAULT '0',
  `good` int NOT NULL DEFAULT '0',
  `hints` int DEFAULT '0',
  `is_active` bit(1) NOT NULL,
  `is_escape` bit(1) NOT NULL,
  `is_horror` bit(1) NOT NULL,
  `is_secret` bit(1) NOT NULL,
  `quiz_type` int DEFAULT '0',
  `review` text,
  `star` int NOT NULL DEFAULT '0',
  `taken_time` int DEFAULT NULL,
  `visit_date` datetime NOT NULL,
  `visitor_num` int NOT NULL DEFAULT '0',
  `member_id` bigint NOT NULL,
  `theme_id` bigint NOT NULL,
  `is_hidden` bit(1) NOT NULL,
  `difficulty` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`theme_comment_id`),
  KEY `FK2set8758cgtujq033rljdxjo9` (`member_id`),
  KEY `FKqy1n32d4tqm89d04wuud2re21` (`theme_id`),
  CONSTRAINT `FK2set8758cgtujq033rljdxjo9` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `FKqy1n32d4tqm89d04wuud2re21` FOREIGN KEY (`theme_id`) REFERENCES `theme` (`theme_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `top_trending`
--

DROP TABLE IF EXISTS `top_trending`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `top_trending` (
  `ttype` varchar(31) NOT NULL,
  `top_trending_id` bigint NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) NOT NULL,
  `refer_id` bigint DEFAULT NULL,
  `reg_date` datetime DEFAULT NULL,
  PRIMARY KEY (`top_trending_id`)
) ENGINE=InnoDB AUTO_INCREMENT=309 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `zim`
--

DROP TABLE IF EXISTS `zim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zim` (
  `ztype` varchar(31) NOT NULL,
  `zim_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `is_zim` bit(1) NOT NULL,
  `refer_id` bigint DEFAULT NULL,
  `member_id` bigint NOT NULL,
  PRIMARY KEY (`zim_id`),
  KEY `FKb6li5sx17c5rfo7qdfnu1sxub` (`member_id`),
  CONSTRAINT `FKb6li5sx17c5rfo7qdfnu1sxub` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-09  4:43:49
