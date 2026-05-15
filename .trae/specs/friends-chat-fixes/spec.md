
# 好友和私信功能修复 - Product Requirement Document

## Overview
- **Summary**: 修复技能交换平台中的三个问题：1) 技能详情页右侧用户的信誉分和时间币显示为0；2) 非好友用户可以点击私信按钮；3) 接受好友申请后好友列表不自动更新
- **Purpose**: 提升用户体验，修复功能缺陷，确保数据显示准确
- **Target Users**: 技能交换平台的所有注册用户

## Goals
- 修复技能详情页右侧用户信息显示问题，正确显示时间币和信誉分
- 添加私信权限验证，确保只有好友才能私信
- 接受好友申请后自动刷新好友列表，无需手动刷新页面

## Non-Goals (Out of Scope)
- 不修改后端API
- 不添加新功能
- 不修改数据库结构

## Background & Context
- 当前技能详情页右侧显示的用户信息（时间币、信誉分）显示为0，需要检查数据来源
- 当前私信按钮没有好友关系验证，任何人都可以点击进入私信页面
- 接受好友申请后，好友列表不会自动更新，需要刷新页面

## Functional Requirements
- **FR-1**: 技能详情页右侧正确显示技能提供者的时间币和信誉分
- **FR-2**: 私信按钮在非好友关系时应该禁用或隐藏
- **FR-3**: 接受好友申请后自动刷新好友列表

## Non-Functional Requirements
- **NFR-1**: 修复应该最小化代码改动
- **NFR-2**: 不影响其他功能的正常运行

## Constraints
- **Technical**: 使用现有的 Vue 3 + Pinia 技术栈
- **Dependencies**: 依赖后端现有的好友关系查询接口

## Assumptions
- 后端API能够正确返回用户的时间币和信誉分数据
- 后端API能够正确判断好友关系

## Acceptance Criteria

### AC-1: 技能详情页正确显示用户时间币和信誉分
- **Given**: 用户打开技能详情页
- **When**: 页面加载完成
- **Then**: 右侧用户卡片应正确显示技能提供者的时间币和信誉分
- **Verification**: `human-judgment`

### AC-2: 非好友用户无法私信
- **Given**: 用户打开非好友用户的技能详情页
- **When**: 查看右侧用户卡片
- **Then**: 私信按钮应该禁用或隐藏，并提示需要先加为好友
- **Verification**: `human-judgment`

### AC-3: 接受好友申请后自动更新好友列表
- **Given**: 用户收到好友申请
- **When**: 用户点击接受按钮
- **Then**: 好友申请应从列表中移除，同时好友应自动出现在好友列表中
- **Verification**: `human-judgment`

## Open Questions
- [ ] 需要确认好友关系查询的后端API
