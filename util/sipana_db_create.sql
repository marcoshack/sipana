-- MySQL dump 10.11
--
-- Host: localhost    Database: sipana
-- ------------------------------------------------------
-- Server version	5.0.51a-3ubuntu5.2

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
-- Current Database: `sipana`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `sipana` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `sipana`;

--
-- Table structure for table `sip_messages`
--

DROP TABLE IF EXISTS `sip_messages`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sip_messages` (
  `sip_message_type` int(11) NOT NULL,
  `id` bigint(20) NOT NULL auto_increment,
  `time` bigint(20) NOT NULL,
  `maxForwards` int(11) NOT NULL,
  `callID` varchar(255) default NULL,
  `srcAddress` varchar(255) default NULL,
  `dstAddress` varchar(255) default NULL,
  `fromUser` varchar(255) default NULL,
  `toUser` varchar(255) default NULL,
  `requestAddr` varchar(255) default NULL,
  `method` varchar(255) default NULL,
  `reasonPhrase` varchar(255) default NULL,
  `statusCode` int(11) default NULL,
  `relatedRequestMethod` varchar(255) default NULL,
  `id_sip_session` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK50964B118991C97` (`id_sip_session`)
) ENGINE=MyISAM AUTO_INCREMENT=14994 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `sip_sessions`
--

DROP TABLE IF EXISTS `sip_sessions`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sip_sessions` (
  `id` bigint(20) NOT NULL auto_increment,
  `method` varchar(255) default NULL,
  `state` int(11) NOT NULL,
  `startTime` bigint(20) NOT NULL,
  `endTime` bigint(20) NOT NULL,
  `disconnectionStart` bigint(20) NOT NULL,
  `establishedTime` bigint(20) NOT NULL,
  `setupTime` bigint(20) NOT NULL,
  `callId` varchar(255) default NULL,
  `fromUser` varchar(255) default NULL,
  `toUser` varchar(255) default NULL,
  `firstResponseTime` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7452 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2008-07-21 13:36:01
