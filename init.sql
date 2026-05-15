-- 社区技能互助平台数据库初始化脚本
-- 数据库：skill_exchange
-- 版本：1.0
-- 日期：2026-05-13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+08:00";

DROP DATABASE IF EXISTS skill_exchange;
CREATE DATABASE skill_exchange DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE skill_exchange;

-- 1. 用户表
CREATE TABLE `user` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `birthday` DATE DEFAULT NULL COMMENT '出生日期',
  `role` ENUM('admin', 'user') NOT NULL DEFAULT 'user' COMMENT '角色：admin-管理员，user-普通用户',
  `time_coin` INT(11) NOT NULL DEFAULT 3 COMMENT '时间币',
  `credit_score` INT(11) NOT NULL DEFAULT 80 COMMENT '信用分',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`),
  KEY `idx_credit_score` (`credit_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 技能类型表
CREATE TABLE `skill_type` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '技能类型ID',
  `name` VARCHAR(50) NOT NULL COMMENT '类型名称',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '类型描述',
  `icon` VARCHAR(255) DEFAULT NULL COMMENT '图标',
  `sort` INT(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能类型表';

-- 3. 技能表
CREATE TABLE `skill` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '技能ID',
  `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '发布者用户ID',
  `type_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '技能类型ID',
  `title` VARCHAR(100) NOT NULL COMMENT '技能标题',
  `description` TEXT COMMENT '技能描述',
  `images` TEXT COMMENT '技能图片（JSON数组）',
  `price` INT(11) NOT NULL DEFAULT 1 COMMENT '技能价格（时间币）',
  `duration` VARCHAR(50) DEFAULT NULL COMMENT '技能时长',
  `location` VARCHAR(255) DEFAULT NULL COMMENT '服务地点',
  `view_count` INT(11) NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `like_count` INT(11) NOT NULL DEFAULT 0 COMMENT '点赞次数',
  `collect_count` INT(11) NOT NULL DEFAULT 0 COMMENT '收藏次数',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type_id` (`type_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_skill_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_skill_type` FOREIGN KEY (`type_id`) REFERENCES `skill_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能表';

-- 4. 技能交换表
CREATE TABLE `skill_exchange` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '交换ID',
  `skill_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '技能ID',
  `provider_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '提供者用户ID',
  `requester_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '请求者用户ID',
  `status` ENUM('pending', 'accepted', 'rejected', 'completed', 'cancelled') NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待处理，accepted-已接受，rejected-已拒绝，completed-已完成，cancelled-已取消',
  `price` INT(11) NOT NULL COMMENT '交易价格（时间币）',
  `scheduled_time` DATETIME DEFAULT NULL COMMENT '预约时间',
  `actual_time` DATETIME DEFAULT NULL COMMENT '实际完成时间',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_skill_id` (`skill_id`),
  KEY `idx_provider_id` (`provider_id`),
  KEY `idx_requester_id` (`requester_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_exchange_skill` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`),
  CONSTRAINT `fk_exchange_provider` FOREIGN KEY (`provider_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_exchange_requester` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能交换表';

-- 5. 时间币记录表
CREATE TABLE `coin_log` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `type` ENUM('income', 'expense') NOT NULL COMMENT '类型：income-收入，expense-支出',
  `amount` INT(11) NOT NULL COMMENT '金额',
  `balance` INT(11) NOT NULL COMMENT '变动后余额',
  `related_type` VARCHAR(50) DEFAULT NULL COMMENT '关联类型',
  `related_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '关联ID',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_coin_log_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='时间币记录表';

-- 6. 聊天消息表
CREATE TABLE `chat_message` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `from_user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '发送者用户ID',
  `to_user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '接收者用户ID',
  `exchange_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '关联交换ID',
  `type` ENUM('text', 'image', 'file') NOT NULL DEFAULT 'text' COMMENT '消息类型：text-文本，image-图片，file-文件',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_from_user_id` (`from_user_id`),
  KEY `idx_to_user_id` (`to_user_id`),
  KEY `idx_exchange_id` (`exchange_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_chat_from_user` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_chat_to_user` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_chat_exchange` FOREIGN KEY (`exchange_id`) REFERENCES `skill_exchange` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';

-- 7. 评论表
CREATE TABLE `comment` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `skill_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '技能ID',
  `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '评论者用户ID',
  `exchange_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '关联交换ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `rating` TINYINT(1) DEFAULT NULL COMMENT '评分：1-5星',
  `parent_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '父评论ID（回复）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_skill_id` (`skill_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_exchange_id` (`exchange_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_comment_skill` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_exchange` FOREIGN KEY (`exchange_id`) REFERENCES `skill_exchange` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_comment_parent` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 8. 技能收藏表
CREATE TABLE `skill_collect` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `skill_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '技能ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_skill` (`user_id`, `skill_id`),
  KEY `idx_skill_id` (`skill_id`),
  CONSTRAINT `fk_collect_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_collect_skill` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能收藏表';

-- 9. 技能点赞表
CREATE TABLE `skill_like` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `skill_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '技能ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_skill` (`user_id`, `skill_id`),
  KEY `idx_skill_id` (`skill_id`),
  CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_like_skill` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能点赞表';

-- 10. 好友申请表
CREATE TABLE `friend_apply` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `from_user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '申请人用户ID',
  `to_user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '被申请人用户ID',
  `status` ENUM('pending', 'accepted', 'rejected') NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待处理，accepted-已接受，rejected-已拒绝',
  `message` VARCHAR(255) DEFAULT NULL COMMENT '申请消息',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_from_user_id` (`from_user_id`),
  KEY `idx_to_user_id` (`to_user_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_friend_apply_from` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_friend_apply_to` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友申请表';

-- 11. 好友表
CREATE TABLE `friend` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '好友关系ID',
  `user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `friend_user_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '好友用户ID',
  `remark` VARCHAR(50) DEFAULT NULL COMMENT '好友备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_friend` (`user_id`, `friend_user_id`),
  KEY `idx_friend_user_id` (`friend_user_id`),
  CONSTRAINT `fk_friend_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_friend_friend` FOREIGN KEY (`friend_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友表';

-- 12. 举报表
CREATE TABLE `report` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '举报ID',
  `reporter_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '举报者用户ID',
  `target_type` VARCHAR(50) NOT NULL COMMENT '举报对象类型：user-用户，skill-技能，comment-评论',
  `target_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '举报对象ID',
  `reason` VARCHAR(255) NOT NULL COMMENT '举报原因',
  `description` TEXT COMMENT '详细描述',
  `status` ENUM('pending', 'processed', 'dismissed') NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待处理，processed-已处理，dismissed-已驳回',
  `handle_result` VARCHAR(255) DEFAULT NULL COMMENT '处理结果',
  `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
  `handler_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '处理人用户ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_reporter_id` (`reporter_id`),
  KEY `idx_target` (`target_type`, `target_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_report_reporter` FOREIGN KEY (`reporter_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_report_handler` FOREIGN KEY (`handler_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='举报表';

-- 13. 系统日志表
CREATE TABLE `sys_log` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '操作用户ID',
  `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
  `operation` VARCHAR(50) DEFAULT NULL COMMENT '操作类型',
  `method` VARCHAR(255) DEFAULT NULL COMMENT '请求方法',
  `params` TEXT COMMENT '请求参数',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-失败，1-成功',
  `error_msg` TEXT COMMENT '错误信息',
  `execute_time` INT(11) DEFAULT NULL COMMENT '执行时间（毫秒）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_module` (`module`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统日志表';

-- 14. 系统通知表
CREATE TABLE `sys_notification` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '接收用户ID（为空表示全体用户）',
  `type` ENUM('system', 'exchange', 'friend', 'message') NOT NULL DEFAULT 'system' COMMENT '通知类型：system-系统通知，exchange-交换通知，friend-好友通知，message-消息通知',
  `title` VARCHAR(100) NOT NULL COMMENT '通知标题',
  `content` TEXT NOT NULL COMMENT '通知内容',
  `related_type` VARCHAR(50) DEFAULT NULL COMMENT '关联类型',
  `related_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '关联ID',
  `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_notification_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统通知表';

-- 15. 反馈表
CREATE TABLE `feedback` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `user_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '反馈用户ID',
  `type` ENUM('bug', 'feature', 'suggestion', 'other') NOT NULL DEFAULT 'suggestion' COMMENT '反馈类型：bug-问题反馈，feature-功能建议，suggestion-意见建议，other-其他',
  `title` VARCHAR(100) NOT NULL COMMENT '反馈标题',
  `content` TEXT NOT NULL COMMENT '反馈内容',
  `images` TEXT COMMENT '反馈图片（JSON数组）',
  `contact` VARCHAR(100) DEFAULT NULL COMMENT '联系方式',
  `status` ENUM('pending', 'processing', 'processed', 'closed') NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待处理，processing-处理中，processed-已处理，closed-已关闭',
  `reply` TEXT COMMENT '回复内容',
  `reply_time` DATETIME DEFAULT NULL COMMENT '回复时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_feedback_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='反馈表';

-- ==================== 插入测试数据 ====================

-- 插入管理员用户
INSERT INTO `user` (`username`, `password`, `nickname`, `avatar`, `email`, `phone`, `role`, `time_coin`, `credit_score`, `status`) VALUES
('admin', '123456', '超级管理员', '/uploads/avatar/admin.png', 'admin@skill.com', '13800138000', 'admin', 999, 100, 1);

-- 插入普通用户
INSERT INTO `user` (`username`, `password`, `nickname`, `avatar`, `email`, `phone`, `role`, `time_coin`, `credit_score`, `status`) VALUES
('zhangsan', '123456', '张三', '/uploads/avatar/zhangsan.png', 'zhangsan@skill.com', '13800138001', 'user', 5, 85, 1),
('lisi', '123456', '李四', '/uploads/avatar/lisi.png', 'lisi@skill.com', '13800138002', 'user', 3, 78, 1),
('wangwu', '123456', '王五', '/uploads/avatar/wangwu.png', 'wangwu@skill.com', '13800138003', 'user', 8, 92, 1),
('zhaoliu', '123456', '赵六', '/uploads/avatar/zhaoliu.png', 'zhaoliu@skill.com', '13800138004', 'user', 2, 75, 1);

-- 插入技能类型
INSERT INTO `skill_type` (`name`, `description`, `icon`, `sort`, `status`) VALUES
('语言学习', '各类语言教学与交流', '/uploads/icon/language.png', 1, 1),
('编程技术', '软件开发与编程技能', '/uploads/icon/programming.png', 2, 1),
('设计创意', '平面设计、UI设计等', '/uploads/icon/design.png', 3, 1),
('生活服务', '各类生活技能服务', '/uploads/icon/life.png', 4, 1),
('运动健身', '健身指导与运动技能', '/uploads/icon/sports.png', 5, 1),
('音乐艺术', '音乐、绘画等艺术技能', '/uploads/icon/art.png', 6, 1),
('职业技能', '职场相关技能培训', '/uploads/icon/career.png', 7, 1),
('其他', '其他类型的技能', '/uploads/icon/other.png', 99, 1);

-- 插入技能
INSERT INTO `skill` (`user_id`, `type_id`, `title`, `description`, `images`, `price`, `duration`, `location`, `view_count`, `like_count`, `collect_count`, `status`) VALUES
(2, 1, '英语口语一对一辅导', '专业英语教师，提供一对一英语口语辅导，针对日常交流和职场英语', '[\"/uploads/skill/english1.jpg\", \"/uploads/skill/english2.jpg\"]', 2, '1小时', '线上', 156, 23, 45, 1),
(3, 2, 'Python入门编程教学', '从零开始学习Python，适合零基础学员，包含基础语法和实战项目', '[\"/uploads/skill/python1.jpg\"]', 3, '1.5小时', '线上', 234, 45, 78, 1),
(4, 3, 'UI设计基础培训', '专业UI设计师，教授Figma使用和设计规范，可完成实战项目', '[\"/uploads/skill/ui1.jpg\", \"/uploads/skill/ui2.jpg\", \"/uploads/skill/ui3.jpg\"]', 4, '2小时', '线上/线下', 89, 18, 32, 1),
(5, 4, '家常菜烹饪教学', '教你做各种家常美食，从入门到精通，健康又美味', '[\"/uploads/skill/cook1.jpg\"]', 2, '1.5小时', '线下', 120, 30, 40, 1),
(2, 5, '瑜伽基础训练', '专业瑜伽教练，提供基础瑜伽课程，适合初学者', '[\"/uploads/skill/yoga1.jpg\"]', 2, '1小时', '线上', 78, 15, 25, 1),
(3, 6, '吉他入门教学', '从零开始学吉他，简单易懂的教学方法，让你快速入门', '[\"/uploads/skill/guitar1.jpg\"]', 3, '1小时', '线上/线下', 167, 28, 52, 1),
(4, 7, '简历制作与面试技巧', 'HR专业指导，帮你制作完美简历，提升面试成功率', '[\"/uploads/skill/resume1.jpg\"]', 2, '1小时', '线上', 210, 35, 60, 1);

-- 插入技能交换记录
INSERT INTO `skill_exchange` (`skill_id`, `provider_id`, `requester_id`, `status`, `price`, `scheduled_time`, `actual_time`, `remark`) VALUES
(1, 2, 3, 'completed', 2, '2026-05-01 14:00:00', '2026-05-01 15:00:00', '英语口语课程'),
(2, 3, 2, 'accepted', 3, '2026-05-15 19:00:00', NULL, 'Python课程'),
(3, 4, 5, 'pending', 4, '2026-05-20 10:00:00', NULL, 'UI设计培训'),
(4, 5, 2, 'completed', 2, '2026-05-05 16:00:00', '2026-05-05 17:30:00', '烹饪教学'),
(1, 2, 4, 'rejected', 2, NULL, NULL, '时间安排不合适');

-- 插入时间币记录
INSERT INTO `coin_log` (`user_id`, `type`, `amount`, `balance`, `related_type`, `related_id`, `description`) VALUES
(2, 'income', 2, 7, 'exchange', 1, '完成英语口语辅导'),
(3, 'expense', 2, 1, 'exchange', 1, '支付英语口语课程'),
(5, 'expense', 2, 0, 'exchange', 4, '支付烹饪教学费用'),
(5, 'income', 2, 2, 'exchange', 4, '完成烹饪教学'),
(2, 'expense', 2, 5, 'exchange', 4, '支付烹饪课程费用');

-- 插入聊天消息
INSERT INTO `chat_message` (`from_user_id`, `to_user_id`, `exchange_id`, `type`, `content`, `is_read`) VALUES
(3, 2, 1, 'text', '你好，我想预约你的英语口语课程', 1),
(2, 3, 1, 'text', '好的，请问你什么时候方便？', 1),
(3, 2, 1, 'text', '明天下午2点可以吗？', 1),
(2, 3, 1, 'text', '没问题，我们明天见！', 1),
(5, 4, 3, 'text', '请问UI设计课程是线上还是线下？', 0);

-- 插入评论
INSERT INTO `comment` (`skill_id`, `user_id`, `exchange_id`, `content`, `rating`, `parent_id`) VALUES
(1, 3, 1, '老师非常专业，口语提升很快！', 5, NULL),
(4, 2, 4, '菜品很美味，老师教得很仔细！', 5, NULL),
(1, 4, NULL, '看起来很不错，下次试试', NULL, NULL),
(1, 2, NULL, '谢谢大家的支持！', NULL, 1);

-- 插入收藏记录
INSERT INTO `skill_collect` (`user_id`, `skill_id`) VALUES
(3, 1), (4, 1), (5, 1),
(2, 2), (4, 2),
(2, 3), (3, 3), (5, 3),
(2, 4), (3, 4),
(3, 5), (4, 5),
(2, 6), (4, 6), (5, 6),
(3, 7), (5, 7);

-- 插入点赞记录
INSERT INTO `skill_like` (`user_id`, `skill_id`) VALUES
(3, 1), (4, 1), (5, 1), (2, 1),
(2, 2), (4, 2), (5, 2),
(2, 3), (3, 3), (5, 3),
(2, 4), (3, 4), (5, 4),
(3, 5), (4, 5),
(2, 6), (4, 6), (5, 6),
(3, 7), (5, 7);

-- 插入好友申请
INSERT INTO `friend_apply` (`from_user_id`, `to_user_id`, `status`, `message`) VALUES
(2, 3, 'accepted', '我是张三，想和你交个朋友'),
(3, 4, 'accepted', '你好，想请教一下UI设计'),
(4, 5, 'pending', '你好，看到你有烹饪技能'),
(2, 5, 'rejected', '你好');

-- 插入好友关系
INSERT INTO `friend` (`user_id`, `friend_user_id`, `remark`, `create_time`) VALUES
(2, 3, '李四', '2026-05-01 10:00:00'),
(3, 2, '张三', '2026-05-01 10:00:00'),
(3, 4, '王五', '2026-05-02 15:00:00'),
(4, 3, '李四', '2026-05-02 15:00:00');

-- 插入举报记录
INSERT INTO `report` (`reporter_id`, `target_type`, `target_id`, `reason`, `description`, `status`) VALUES
(2, 'user', 5, '其他', '该用户发布的技能价格过高', 'pending'),
(3, 'skill', 4, '内容不实', '技能描述与实际不符', 'dismissed');

-- 插入系统日志
INSERT INTO `sys_log` (`user_id`, `module`, `operation`, `method`, `params`, `ip`, `status`, `execute_time`) VALUES
(1, '用户管理', '登录', 'POST', '{\'username\': \'admin\'}', '127.0.0.1', 1, 150),
(2, '技能管理', '发布技能', 'POST', '{\'title\': \'英语口语一对一辅导\'}', '192.168.1.100', 1, 200),
(3, '交换管理', '申请交换', 'POST', '{\'skill_id\': 1}', '192.168.1.101', 1, 180);

-- 插入系统通知
INSERT INTO `sys_notification` (`user_id`, `type`, `title`, `content`, `related_type`, `related_id`, `is_read`) VALUES
(NULL, 'system', '欢迎加入技能互助平台', '欢迎您加入我们的社区！您获得了3个初始时间币。', NULL, NULL, 0),
(2, 'exchange', '您的交换已完成', '您与李四的英语口语交换已完成，获得2个时间币。', 'exchange', 1, 1),
(3, 'exchange', '您有新的交换申请', '张三申请了您的Python课程，请及时处理。', 'exchange', 2, 0),
(4, 'friend', '好友申请已通过', '李四已接受您的好友申请。', 'friend', NULL, 1);

-- 插入反馈记录
INSERT INTO `feedback` (`user_id`, `type`, `title`, `content`, `contact`, `status`) VALUES
(2, 'suggestion', '希望增加技能筛选功能', '建议在技能列表增加价格区间筛选功能，方便找到合适的技能。', 'zhangsan@skill.com', 'pending'),
(3, 'bug', '消息通知不及时', '有时候收到消息没有及时推送，希望修复这个问题。', 'lisi@skill.com', 'processing'),
(NULL, 'feature', '希望增加视频教学功能', '建议平台支持视频课程功能，可以更好地进行技能教学。', NULL, 'pending');

COMMIT;
