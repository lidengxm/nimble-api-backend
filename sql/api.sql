-- MySQL dump 10.13  Distrib 5.7.19, for Win64 (x86_64)
--
-- Host: localhost    Database: api
-- ------------------------------------------------------
-- Server version	5.7.19

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
-- Table structure for table `interface_info`
--

DROP TABLE IF EXISTS `interface_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interface_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id主键',
  `name` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `description` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `url` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接口地址',
  `requestHeader` text COLLATE utf8mb4_unicode_ci COMMENT '请求头',
  `responseHeader` text COLLATE utf8mb4_unicode_ci COMMENT '响应头',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '接口状态（0-关闭，1-开启）',
  `method` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求类型',
  `userId` bigint(20) NOT NULL COMMENT '创建人',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
  `requestParams` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求参数',
  `sdk` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接口对应的SDK类路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接口信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interface_info`
--

LOCK TABLES `interface_info` WRITE;
/*!40000 ALTER TABLE `interface_info` DISABLE KEYS */;
INSERT INTO `interface_info` VALUES (1,'getName','返回你的姓名','/api/interface/name/user','{\"Content-Type\":\"application/json\"}','{\"Content-Type\":\"application/json\"}',1,'Post',16855580200,'2023-07-30 15:58:07','2023-08-11 20:14:49',0,'{\"name\": \"name\", \"type\": \"string\"}\n','com.lmeng.nimbleclientsdk.client.NameApiClient'),(2,'getPoisonChicken','返回一句毒鸡汤','/api/interface/yan/api.php','无','无',1,'Get',16855580200,'2023-08-08 15:07:31','2023-08-11 19:41:48',0,'无','com.lmeng.nimbleclientsdk.client.SentenceApiClient'),(3,'getRandomWork','返回随机一句文本','/api/interface/random/word','无','无',1,'Get',16855580200,'2023-08-09 15:40:37','2023-08-11 19:41:48',0,'无','com.lmeng.nimbleclientsdk.client.RandomApiClient'),(4,'getRandomImageUrl','返回随机一张壁纸URL','/api/interface/random/image','无','无',1,'Post',16855580200,'2023-08-09 16:16:56','2023-08-11 19:41:48',0,'无','com.lmeng.nimbleclientsdk.client.RandomApiClient');
/*!40000 ALTER TABLE `interface_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `tags` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签列表（json 数组）',
  `thumbNum` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `favourNum` int(11) NOT NULL DEFAULT '0' COMMENT '收藏数',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_favour`
--

DROP TABLE IF EXISTS `post_favour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_favour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint(20) NOT NULL COMMENT '帖子 id',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_postId` (`postId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='帖子收藏';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_favour`
--

LOCK TABLES `post_favour` WRITE;
/*!40000 ALTER TABLE `post_favour` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_favour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_thumb`
--

DROP TABLE IF EXISTS `post_thumb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_thumb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint(20) NOT NULL COMMENT '帖子 id',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_postId` (`postId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='帖子点赞';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_thumb`
--

LOCK TABLES `post_thumb` WRITE;
/*!40000 ALTER TABLE `post_thumb` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_thumb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `userAccount` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `unionId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信开放平台id',
  `mpOpenId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公众号openId',
  `userName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
  `accessKey` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'accessKey',
  `secretKey` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'secretKey',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_unionId` (`unionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (8,'admin12','20eb15df6d9fbb862fabc4a511014571',NULL,NULL,'APIqs3z','https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202307091458670.webp',NULL,'user','faddd4538c581f71e3c65981328f7da0','9dda64dd062eee614644fdb3c4130bb3','2023-08-11 19:48:59','2023-08-11 19:52:15',0),(16855580200,'yupi','20eb15df6d9fbb862fabc4a511014571',NULL,NULL,'玛卡巴卡','https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/Nl3Izdy2-QQ图片20230709150002.jpg','江湖见','admin','7515e262dddb4a86dde1681988a50cd2','c43cead80402ecbde68cf964cf591e07','2023-07-30 15:51:24','2023-08-11 19:41:17',0),(75697581917,'admin','20eb15df6d9fbb862fabc4a511014571',NULL,NULL,'APIwltv','https://alylmengbucket.oss-cn-nanjing.aliyuncs.com/pictures/202307091458670.webp',NULL,'user','1cbef0049b9fdf03a748b907a722f9cf','ef1f1a319c8a4857501cd673a21fe2a4','2023-08-11 19:52:53','2023-08-11 19:52:53',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_interface_info`
--

DROP TABLE IF EXISTS `user_interface_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_interface_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '调用用户 id',
  `interfaceInfoId` bigint(20) NOT NULL COMMENT '接口 id',
  `totalNum` int(11) NOT NULL DEFAULT '0' COMMENT '总调用次数',
  `leftNum` int(11) NOT NULL DEFAULT '0' COMMENT '剩余调用次数',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0-正常，1-禁用',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COMMENT='用户调用接口关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_interface_info`
--

LOCK TABLES `user_interface_info` WRITE;
/*!40000 ALTER TABLE `user_interface_info` DISABLE KEYS */;
INSERT INTO `user_interface_info` VALUES (1,16855580200,1,122,109,0,'2023-07-31 15:37:05','2023-08-11 19:42:04',0),(2,16855580200,2,115,99,0,'2023-08-04 15:44:52','2023-08-13 20:38:55',0),(3,16855580200,3,44,60,0,'2023-08-04 20:48:43','2023-08-13 20:43:17',0),(4,16855580200,4,68,65,0,'2023-08-08 19:34:57','2023-08-13 20:39:17',0),(5,75697581917,2,1,99,0,'2023-08-11 20:02:20','2023-08-11 20:20:28',0),(6,75697581917,1,0,20,0,'2023-08-11 20:03:28','2023-08-11 20:20:28',0);
/*!40000 ALTER TABLE `user_interface_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-14 11:24:55
