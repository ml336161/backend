
# 个人资料和好友功能增强 - Product Requirement Document

## Overview
- **Summary**: 实现三个增强功能：1) 编辑资料增加改密码和头像上传功能，个人中心显示年龄；2) 点击好友头像显示详细资料页；3) 技能交换流程优化（进行中->已完成状态切换并扣除时间币）
- **Purpose**: 提升用户体验，完善个人资料管理，优化技能交换流程
- **Target Users**: 技能交换平台的所有注册用户

## Goals
- 完善个人资料编辑功能（改密码、头像上传、年龄显示）
- 实现好友详情资料页
- 优化技能交换流程，支持状态切换和时间币扣除

## Non-Goals (Out of Scope)
- 不修改数据库表结构
- 不添加全新的业务逻辑

## Background & Context
- 当前编辑资料只有昵称、邮箱、出生日期三个字段
- 当前点击好友头像只能进入私信页面
- 当前技能交换没有进行中状态，直接从接受变为已完成

## Functional Requirements
- **FR-1**: 编辑资料增加修改密码功能
- **FR-2**: 编辑资料增加头像上传功能
- **FR-3**: 个人中心显示用户年龄（根据出生日期计算）
- **FR-4**: 点击好友头像显示好友详情页（名字、年龄、已发布技能、点赞、收藏）
- **FR-5**: 好友详情页的点赞/收藏技能可点击跳转
- **FR-6**: 技能交换接受后状态变为"进行中"
- **FR-7**: 点击"进行中"的交换可变为"已完成"并扣除时间币

## Non-Functional Requirements
- **NFR-1**: 功能应该易用且直观
- **NFR-2**: 头像上传应该支持常见图片格式
- **NFR-3**: 年龄计算应该准确

## Constraints
- **Technical**: 使用现有的 Vue 3 + Spring Boot 技术栈
- **Dependencies**: 依赖现有的上传服务和用户服务

## Assumptions
- 后端已有头像上传接口
- 用户出生日期已存储

## Acceptance Criteria

### AC-1: 修改密码功能
- **Given**: 用户已登录并打开编辑资料
- **When**: 用户输入原密码和新密码并提交
- **Then**: 密码应该成功修改
- **Verification**: `human-judgment`

### AC-2: 头像上传功能
- **Given**: 用户已登录并打开编辑资料
- **When**: 用户选择图片并上传
- **Then**: 头像应该成功更新并显示
- **Verification**: `human-judgment`

### AC-3: 个人中心显示年龄
- **Given**: 用户已登录并进入个人中心
- **When**: 页面加载完成
- **Then**: 应该显示用户年龄（根据出生日期计算）
- **Verification**: `human-judgment`

### AC-4: 好友详情页
- **Given**: 用户点击好友头像
- **When**: 页面加载完成
- **Then**: 应该显示好友的基本信息、技能、点赞和收藏
- **Verification**: `human-judgment`

### AC-5: 技能交换状态流程
- **Given**: 用户接受技能交换申请
- **When**: 用户点击"完成"按钮
- **Then**: 状态应变为已完成并扣除时间币
- **Verification**: `human-judgment`

## Open Questions
- [ ] 需要确认头像上传的后端接口
