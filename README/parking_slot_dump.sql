-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: parking
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `parking-floor`
--

DROP TABLE IF EXISTS `parking-floor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking-floor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time_stamp` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_time_stamp` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking-floor`
--

LOCK TABLES `parking-floor` WRITE;
/*!40000 ALTER TABLE `parking-floor` DISABLE KEYS */;
INSERT INTO `parking-floor` VALUES (1,'2025-09-26 02:22:40.641000','Floor 1',NULL),(2,'2025-09-26 12:53:27.023000','Floor 2',NULL);
/*!40000 ALTER TABLE `parking-floor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking-slot`
--

DROP TABLE IF EXISTS `parking-slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking-slot` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `available` bit(1) DEFAULT NULL,
  `slot_number` varchar(255) DEFAULT NULL,
  `vehicle_type` enum('FOUR_WHEELER','TWO_WHEELER') DEFAULT NULL,
  `floor_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6ub8v28vswmw2b36prqrrtma9` (`floor_id`),
  CONSTRAINT `FK6ub8v28vswmw2b36prqrrtma9` FOREIGN KEY (`floor_id`) REFERENCES `parking-floor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking-slot`
--

LOCK TABLES `parking-slot` WRITE;
/*!40000 ALTER TABLE `parking-slot` DISABLE KEYS */;
INSERT INTO `parking-slot` VALUES (1,_binary '','S001','TWO_WHEELER',1),(2,_binary '','S002','FOUR_WHEELER',1),(3,_binary '','S001','TWO_WHEELER',2);
/*!40000 ALTER TABLE `parking-slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cost` double NOT NULL,
  `end_time` datetime(6) NOT NULL,
  `start_time` datetime(6) NOT NULL,
  `vehicle_number` varchar(255) NOT NULL,
  `slot_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2mbq7cl2g79ks1m1kmm4brfd8` (`slot_id`),
  CONSTRAINT `FK2mbq7cl2g79ks1m1kmm4brfd8` FOREIGN KEY (`slot_id`) REFERENCES `parking-slot` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,40,'2025-09-26 03:20:10.500000','2025-09-26 02:02:10.500000','HW59RZ0304',1);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-26 20:32:59
