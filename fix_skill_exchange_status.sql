-- 修复技能交换状态枚举问题
-- 此脚本用于更新现有数据库的 skill_exchange 表的 status 字段枚举

USE skill_exchange;

-- 修改 status 字段，添加 in_progress 状态
ALTER TABLE `skill_exchange` 
MODIFY COLUMN `status` ENUM(
    'pending', 
    'accepted', 
    'in_progress', 
    'rejected', 
    'completed', 
    'cancelled'
) NOT NULL DEFAULT 'pending' 
COMMENT '状态：pending-待处理，accepted-已接受，in_progress-进行中，rejected-已拒绝，completed-已完成，cancelled-已取消';
