--
-- Current Database: `sipana`
--

DROP DATABASE IF EXISTS sipana;
CREATE DATABASE `sipana`;
USE `sipana`;

--
-- Table structure for table `sip_sessions`
--

DROP TABLE IF EXISTS `sip_sessions`;
CREATE TABLE `sip_sessions` (
  `id` bigint(20) NOT NULL auto_increment,
  `requestMethod` varchar(255) default NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `sip_messages`
--

DROP TABLE IF EXISTS `sip_messages`;
CREATE TABLE `sip_messages` (
  `sip_message_type` int(11) NOT NULL,
  `id` bigint(20) NOT NULL auto_increment,
  `time` bigint(20) NOT NULL,
  `maxForwards` int(11) NOT NULL,
  `callID` varchar(255) default NULL,
  `srcAddress` varchar(255) default NULL,
  `srcPort` int(11) default NULL,
  `dstAddress` varchar(255) default NULL,
  `dstPort` int(11) default NULL,
  `fromUser` varchar(255) default NULL,
  `toUser` varchar(255) default NULL,
  `fromDisplay` varchar(255) default NULL,
  `toDisplay` varchar(255) default NULL,
  `requestAddr` varchar(255) default NULL,
  `method` varchar(255) default NULL,
  `reasonPhrase` varchar(255) default NULL,
  `statusCode` int(11) default NULL,
  `relatedRequestMethod` varchar(255) default NULL,
  `id_sip_session` bigint(20) default NULL,

  PRIMARY KEY  (`id`),
  FOREIGN KEY `fk_sip_messages_id_sip_session` (`id_sip_session`)
      REFERENCES `sipana`.`sip_sessions` (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

