-- MySQL dump 10.13  Distrib 8.0.19, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: ksmledgerdb
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ksm_admin`
--

DROP TABLE IF EXISTS `ksm_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ksm_admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `membership_id` varchar(45) NOT NULL,
  `password` varchar(191) NOT NULL,
  `administrative_role` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `password_UNIQUE` (`password`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ksm_admin`
--

LOCK TABLES `ksm_admin` WRITE;
/*!40000 ALTER TABLE `ksm_admin` DISABLE KEYS */;
INSERT INTO `ksm_admin` VALUES (1,'KSM12345AB','legy12345','president');
/*!40000 ALTER TABLE `ksm_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ksm_ledgers`
--

DROP TABLE IF EXISTS `ksm_ledgers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ksm_ledgers` (
  `sn` int NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `debit` decimal(2,0) NOT NULL,
  `credit` decimal(2,0) NOT NULL,
  `particulars` varchar(45) NOT NULL,
  `balance` decimal(2,0) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`sn`,`user_id`),
  UNIQUE KEY `sn_UNIQUE` (`sn`),
  KEY `fk_ksm_ledgers_ksm_users1_idx` (`user_id`),
  CONSTRAINT `fk_ksm_ledgers_ksm_users1` FOREIGN KEY (`user_id`) REFERENCES `ksm_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ksm_ledgers`
--

LOCK TABLES `ksm_ledgers` WRITE;
/*!40000 ALTER TABLE `ksm_ledgers` DISABLE KEYS */;
/*!40000 ALTER TABLE `ksm_ledgers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ksm_roles`
--

DROP TABLE IF EXISTS `ksm_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ksm_roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name_UNIQUE` (`role_name`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ksm_roles`
--

LOCK TABLES `ksm_roles` WRITE;
/*!40000 ALTER TABLE `ksm_roles` DISABLE KEYS */;
INSERT INTO `ksm_roles` VALUES (1,'admin'),(2,'member');
/*!40000 ALTER TABLE `ksm_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ksm_users`
--

DROP TABLE IF EXISTS `ksm_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ksm_users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) NOT NULL,
  `middlename` varchar(45) DEFAULT NULL,
  `lastname` varchar(45) NOT NULL,
  `membership_id` varchar(45) NOT NULL,
  `email` varchar(191) DEFAULT NULL,
  `phone` varchar(15) NOT NULL,
  `initiation_date` timestamp NOT NULL,
  `address` longtext,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `avatar_path` varchar(191) DEFAULT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`,`role_id`),
  UNIQUE KEY `membership_id_UNIQUE` (`membership_id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `phone_UNIQUE` (`phone`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_ksm_users_ksm_roles_idx` (`role_id`),
  CONSTRAINT `fk_ksm_users_ksm_roles` FOREIGN KEY (`role_id`) REFERENCES `ksm_roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ksm_users`
--

LOCK TABLES `ksm_users` WRITE;
/*!40000 ALTER TABLE `ksm_users` DISABLE KEYS */;
INSERT INTO `ksm_users` VALUES (1,'John','E','Kamsi','KSM12345AB','johnkamsi@gmail.com','07031293784','2019-01-01 08:00:01','Umuahia',NULL,NULL,NULL,'1973-03-01',NULL,1);
/*!40000 ALTER TABLE `ksm_users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-20  4:59:45
