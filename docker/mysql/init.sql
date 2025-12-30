-- MySQL初始化脚本 - 活动报名系统

-- 创建用户服务数据库
CREATE DATABASE IF NOT EXISTS `user_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `user_db_1` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `user_db_2` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建活动服务数据库
CREATE DATABASE IF NOT EXISTS `activity_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `activity_db_1` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `activity_db_2` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建报名服务数据库
CREATE DATABASE IF NOT EXISTS `registration_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 用户服务 - 用户表
USE `user_db`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `college_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 分库表 - 用户表
USE `user_db_1`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `college_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

USE `user_db_2`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `college_id` INT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 活动服务 - 活动表
USE `activity_db`;
CREATE TABLE IF NOT EXISTS `activities` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `activity_id` BIGINT NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `max_participants` INT NOT NULL,
  `current_participants` INT DEFAULT 0,
  `status` VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  `created_by` BIGINT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_id` (`activity_id`),
  KEY `idx_status` (`status`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 分库表 - 活动表
USE `activity_db_1`;
CREATE TABLE IF NOT EXISTS `activities` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `activity_id` BIGINT NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `max_participants` INT NOT NULL,
  `current_participants` INT DEFAULT 0,
  `status` VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  `created_by` BIGINT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_id` (`activity_id`),
  KEY `idx_status` (`status`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

USE `activity_db_2`;
CREATE TABLE IF NOT EXISTS `activities` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `activity_id` BIGINT NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT NOT NULL,
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `max_participants` INT NOT NULL,
  `current_participants` INT DEFAULT 0,
  `status` VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  `created_by` BIGINT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_id` (`activity_id`),
  KEY `idx_status` (`status`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 报名服务 - 报名记录表
USE `registration_db`;
CREATE TABLE IF NOT EXISTS `registrations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `activity_id` BIGINT NOT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_activity` (`user_id`, `activity_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入测试数据 - 用户表
USE `user_db`;
INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `college_id`) VALUES
(1, 'test_user_1', 'test1@example.com', '$2a$10$J8C7aQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ', 1),
(2, 'test_user_2', 'test2@example.com', '$2a$10$J8C7aQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ7eQ', 2);

-- 插入测试数据 - 活动表
USE `activity_db`;
INSERT INTO `activities` (`activity_id`, `title`, `description`, `start_time`, `end_time`, `max_participants`, `status`, `created_by`) VALUES
(1, '测试活动1', '这是一个测试活动', '2025-01-01 10:00:00', '2025-01-02 10:00:00', 100, 'PUBLISHED', 1),
(2, '测试活动2', '这是第二个测试活动', '2025-01-03 10:00:00', '2025-01-04 10:00:00', 200, 'PUBLISHED', 1);

-- 授权用户访问数据库
GRANT ALL PRIVILEGES ON `user_db`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `user_db_1`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `user_db_2`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `activity_db`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `activity_db_1`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `activity_db_2`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `registration_db`.* TO 'user'@'%';

FLUSH PRIVILEGES;