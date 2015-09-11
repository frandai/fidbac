-- MySQL dump 10.13  Distrib 5.5.44, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: fidbac
-- ------------------------------------------------------
-- Server version	5.5.44-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `anoption`
--

DROP TABLE IF EXISTS `anoption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anoption` (
  `presentation_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `option_id` int(11) NOT NULL,
  `anoption` text,
  `correct` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`presentation_id`,`question_id`,`option_id`),
  KEY `question_option` (`question_id`,`presentation_id`),
  CONSTRAINT `option_question` FOREIGN KEY (`presentation_id`, `question_id`) REFERENCES `question` (`presentation_id`, `question_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anoption`
--

LOCK TABLES `anoption` WRITE;
/*!40000 ALTER TABLE `anoption` DISABLE KEYS */;
/*!40000 ALTER TABLE `anoption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answer` (
  `user_id` int(11) NOT NULL,
  `presentation_id` int(11) NOT NULL,
  `quiestion_id` int(11) NOT NULL,
  `option_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`presentation_id`,`quiestion_id`,`option_id`),
  KEY `answer` (`presentation_id`,`quiestion_id`,`option_id`),
  CONSTRAINT `answer` FOREIGN KEY (`presentation_id`, `quiestion_id`, `option_id`) REFERENCES `anoption` (`presentation_id`, `question_id`, `option_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_answer` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feeling`
--

DROP TABLE IF EXISTS `feeling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feeling` (
  `feeling_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `color` varchar(255) NOT NULL,
  `icon` varchar(255) NOT NULL,
  PRIMARY KEY (`feeling_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feeling`
--

LOCK TABLES `feeling` WRITE;
/*!40000 ALTER TABLE `feeling` DISABLE KEYS */;
INSERT INTO `feeling` VALUES (1,'Very interesting','#003fff','2'),(2,'Interesting','#05ff00','1'),(3,'Indiferent','#ff00e9','13'),(4,'Not interesting','#ffea00','15'),(5,'Boring','#ff001f','8');
/*!40000 ALTER TABLE `feeling` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filter`
--

DROP TABLE IF EXISTS `filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filter` (
  `presentation_id` int(11) NOT NULL,
  `property_id` int(11) NOT NULL,
  `operation` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `filter_id` int(11) NOT NULL,
  PRIMARY KEY (`presentation_id`,`filter_id`),
  KEY `property_filter` (`property_id`),
  CONSTRAINT `presentation_filter` FOREIGN KEY (`presentation_id`) REFERENCES `presentation` (`presentation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `property_filter` FOREIGN KEY (`property_id`) REFERENCES `property` (`property_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filter`
--

LOCK TABLES `filter` WRITE;
/*!40000 ALTER TABLE `filter` DISABLE KEYS */;
/*!40000 ALTER TABLE `filter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `presentation`
--

DROP TABLE IF EXISTS `presentation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `presentation` (
  `presentation_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  PRIMARY KEY (`presentation_id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `presentation`
--

LOCK TABLES `presentation` WRITE;
/*!40000 ALTER TABLE `presentation` DISABLE KEYS */;
/*!40000 ALTER TABLE `presentation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `presentation_have_feeling`
--

DROP TABLE IF EXISTS `presentation_have_feeling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `presentation_have_feeling` (
  `presentation_id` int(11) NOT NULL,
  `feeling_id` int(11) NOT NULL,
  `anorder` int(11) NOT NULL,
  `isdefault` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`presentation_id`,`feeling_id`),
  KEY `feeling_presentation` (`feeling_id`),
  CONSTRAINT `feeling_presentation` FOREIGN KEY (`feeling_id`) REFERENCES `feeling` (`feeling_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `presentation_feeling` FOREIGN KEY (`presentation_id`) REFERENCES `presentation` (`presentation_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `presentation_have_feeling`
--

LOCK TABLES `presentation_have_feeling` WRITE;
/*!40000 ALTER TABLE `presentation_have_feeling` DISABLE KEYS */;
/*!40000 ALTER TABLE `presentation_have_feeling` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property`
--

DROP TABLE IF EXISTS `property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property` (
  `property_id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `type` int(11) NOT NULL,
  `isdefault` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`property_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property`
--

LOCK TABLES `property` WRITE;
/*!40000 ALTER TABLE `property` DISABLE KEYS */;
INSERT INTO `property` VALUES (1,'Gender',2,1),(2,'Study Level',2,1);
/*!40000 ALTER TABLE `property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property_mandatory`
--

DROP TABLE IF EXISTS `property_mandatory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property_mandatory` (
  `presentation_id` int(11) NOT NULL,
  `property_id` int(11) NOT NULL,
  PRIMARY KEY (`presentation_id`,`property_id`),
  KEY `presentation_property` (`property_id`),
  CONSTRAINT `presentation_property` FOREIGN KEY (`property_id`) REFERENCES `property` (`property_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `property_presentation` FOREIGN KEY (`presentation_id`) REFERENCES `presentation` (`presentation_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_mandatory`
--

LOCK TABLES `property_mandatory` WRITE;
/*!40000 ALTER TABLE `property_mandatory` DISABLE KEYS */;
/*!40000 ALTER TABLE `property_mandatory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `presentation_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `question` text,
  PRIMARY KEY (`presentation_id`,`question_id`),
  CONSTRAINT `presentation_question` FOREIGN KEY (`presentation_id`) REFERENCES `presentation` (`presentation_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section` (
  `presentation_id` int(11) NOT NULL,
  `section_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  PRIMARY KEY (`presentation_id`,`section_id`),
  CONSTRAINT `presentation_section` FOREIGN KEY (`presentation_id`) REFERENCES `presentation` (`presentation_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section`
--

LOCK TABLES `section` WRITE;
/*!40000 ALTER TABLE `section` DISABLE KEYS */;
/*!40000 ALTER TABLE `section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username_unique` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','21232f297a57a5a743894a0e4a801fc3');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_admin_presentation`
--

DROP TABLE IF EXISTS `user_admin_presentation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_admin_presentation` (
  `user_id` int(11) NOT NULL,
  `presentation_id` int(11) NOT NULL,
  PRIMARY KEY (`presentation_id`,`user_id`),
  KEY `user_admin_presentation` (`user_id`),
  CONSTRAINT `presentation_admin_user` FOREIGN KEY (`presentation_id`) REFERENCES `presentation` (`presentation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_admin_presentation` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_admin_presentation`
--

LOCK TABLES `user_admin_presentation` WRITE;
/*!40000 ALTER TABLE `user_admin_presentation` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_admin_presentation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_have_property`
--

DROP TABLE IF EXISTS `user_have_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_have_property` (
  `user_id` int(11) NOT NULL,
  `property_id` int(11) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`property_id`),
  KEY `property_user` (`property_id`),
  CONSTRAINT `property_user` FOREIGN KEY (`property_id`) REFERENCES `property` (`property_id`) ON UPDATE CASCADE,
  CONSTRAINT `user_property` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_have_property`
--

LOCK TABLES `user_have_property` WRITE;
/*!40000 ALTER TABLE `user_have_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_have_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_participates_presentation`
--

DROP TABLE IF EXISTS `user_participates_presentation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_participates_presentation` (
  `user_id` int(11) NOT NULL,
  `presentation_id` int(11) NOT NULL,
  PRIMARY KEY (`presentation_id`,`user_id`),
  KEY `user_admin_presentation` (`user_id`),
  CONSTRAINT `presentation_participate` FOREIGN KEY (`presentation_id`) REFERENCES `presentation` (`presentation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_particiapte` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_participates_presentation`
--

LOCK TABLES `user_participates_presentation` WRITE;
/*!40000 ALTER TABLE `user_participates_presentation` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_participates_presentation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `value`
--

DROP TABLE IF EXISTS `value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `value` (
  `value_id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(255) NOT NULL,
  `property_id` int(11) NOT NULL,
  PRIMARY KEY (`value_id`,`property_id`),
  KEY `property` (`property_id`),
  CONSTRAINT `property` FOREIGN KEY (`property_id`) REFERENCES `property` (`property_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `value`
--

LOCK TABLES `value` WRITE;
/*!40000 ALTER TABLE `value` DISABLE KEYS */;
INSERT INTO `value` VALUES (1,'Female',1),(1,'Basic',2),(2,'Male',1),(2,'High School',2),(3,'Bachelor\'s Degree',2),(4,'Master\'s Degree',2),(5,'Doctoral Degree',2);
/*!40000 ALTER TABLE `value` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-09-10  0:01:44
