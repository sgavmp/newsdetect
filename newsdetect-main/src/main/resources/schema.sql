-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `alerts`;
CREATE TABLE `alerts` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `query` longtext NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Table structure for table `alert`
--

DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
  `id` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `type_event` varchar(20) NOT NULL,
  `origin_id` bigint(20) NOT NULL,
  `info` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `feed`
--

DROP TABLE IF EXISTS `feeds`;
CREATE TABLE `feeds` (
  `id` bigint(20) NOT NULL,
  `name` varchar(60) NOT NULL,
  `date_format` varchar(50) DEFAULT NULL,
  `url_scrap` longtext NOT NULL,
  `url_feed` VARCHAR(255) DEFAULT NULL,
  `scrap_type` varchar(50) NOT NULL,
  `feed_type` varchar(50) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `min_refresh` int(11) NOT NULL,
  `selector_title` varchar(255) DEFAULT NULL,
  `selector_content` varchar(255) DEFAULT NULL,
  `selector_pub_date` varchar(255) DEFAULT NULL,
  `selector_title_meta` BOOLEAN DEFAULT NULL,
  `selector_content_meta` BOOLEAN DEFAULT NULL,
  `selector_pub_date_meta` BOOLEAN DEFAULT NULL,
  `char_set` varchar(50) DEFAULT NULL,
  `state` varchar(50) NOT NULL,
  `extraction_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `feed_crawler_news`
--

DROP TABLE IF EXISTS `feed_crawler_links`;
CREATE TABLE `feed_crawler_links` (
  `feed_id` bigint(20) NOT NULL,
  `crawler_link` longtext NOT NULL,
  FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`),
  CONSTRAINT UNIQUE INDEX (`feed_id`,`crawler_link`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `feed_place`
--

DROP TABLE IF EXISTS `feed_places`;
CREATE TABLE `feed_places` (
  `feed_id` bigint(20) NOT NULL,
  `feed_place` VARCHAR(50) DEFAULT NULL,
  FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`),
  CONSTRAINT UNIQUE INDEX (`feed_id`,`feed_place`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `locations`;
CREATE TABLE `locations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `query` LONGTEXT DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL,
  `title` longtext,
  `content` longtext,
  `feed_id` bigint(20) NOT NULL,
  `url` longtext NOT NULL,
  `pub_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `detected_news`
--

DROP TABLE IF EXISTS `detected_news`;
CREATE TABLE `detected_news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `title` longtext,
  `link` longtext,
  `date_pub` datetime DEFAULT NULL,
  `alert_id` bigint(20) NOT NULL,
  `status` VARCHAR(50) NOT NULL,
  `score` float DEFAULT '0',
  `score_lucene` float DEFAULT '0',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`),
  FOREIGN KEY (`alert_id`) REFERENCES `alerts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `detected_news_locations`
--

DROP TABLE IF EXISTS `news_detect_locations`;
CREATE TABLE `news_detect_locations` (
  `location_id` bigint(20) NOT NULL,
  `news_link` longtext,
  PRIMARY KEY (`location_id`,`news_link`(255)),
  FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `topic`
--

DROP TABLE IF EXISTS `topics`;
CREATE TABLE `topics` (
  `title` varchar(255),
  `query` longtext NOT NULL,
  PRIMARY KEY (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `topic_references`
--

DROP TABLE IF EXISTS `topic_references`;
CREATE TABLE `topic_references` (
  `topic_id` varchar(255) NOT NULL,
  `topic_references_id` varchar(255) NOT NULL,
  FOREIGN KEY (`topic_references_id`) REFERENCES `topics` (`title`),
  FOREIGN KEY (`topic_id`) REFERENCES `topics` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` BIGINT(20),
  `user_name` varchar(255) NOT NULL UNIQUE,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL UNIQUE ,
  `is_admin` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
