
# 个人资料和好友功能增强 - The Implementation Plan (Decomposed and Prioritized Task List)

## [x] Task 1: 编辑资料增加修改密码功能
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 在 Profile.vue 的编辑资料弹窗中添加密码修改字段（原密码、新密码、确认新密码）
  - 在 UserController 中添加修改密码接口
  - 在 UserService 中实现修改密码逻辑
- **Acceptance Criteria Addressed**: [AC-1]
- **Test Requirements**:
  - `human-judgement` TR-1.1: 用户可以在编辑资料中修改密码
  - `human-judgement` TR-1.2: 修改密码需要验证原密码
- **Notes**: 需要验证原密码正确性

## [x] Task 2: 编辑资料增加头像上传功能
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 在 Profile.vue 的编辑资料弹窗中添加头像上传组件
  - 使用现有上传接口上传头像
  - 更新用户头像字段
- **Acceptance Criteria Addressed**: [AC-2]
- **Test Requirements**:
  - `human-judgement` TR-2.1: 用户可以上传新头像
  - `human-judgement` TR-2.2: 头像上传后立即显示更新
- **Notes**: 使用 Element Plus 的上传组件

## [x] Task 3: 个人中心显示年龄
- **Priority**: P1
- **Depends On**: None
- **Description**: 
  - 在 Profile.vue 中添加年龄计算逻辑
  - 根据用户出生日期计算年龄
  - 在基本信息区域显示年龄
- **Acceptance Criteria Addressed**: [AC-3]
- **Test Requirements**:
  - `human-judgement` TR-3.1: 个人中心正确显示用户年龄
- **Notes**: 需要处理未设置出生日期的情况

## [x] Task 4: 创建好友详情页组件
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 创建 FriendProfile.vue 组件
  - 显示好友基本信息（名字、年龄）
  - 显示好友发布的技能列表
  - 显示好友的点赞和收藏技能列表
- **Acceptance Criteria Addressed**: [AC-4]
- **Test Requirements**:
  - `human-judgement` TR-4.1: 好友详情页正确显示所有信息
  - `human-judgement` TR-4.2: 点击技能可以跳转
- **Notes**: 需要添加路由

## [x] Task 5: 修改好友头像点击行为
- **Priority**: P1
- **Depends On**: Task 4
- **Description**: 
  - 修改 Friends.vue 中好友头像的点击事件
  - 点击头像跳转到好友详情页而不是私信
- **Acceptance Criteria Addressed**: [AC-4]
- **Test Requirements**:
  - `human-judgement` TR-5.1: 点击好友头像跳转到详情页
- **Notes**: 需要添加导航路由

## [x] Task 6: 技能交换状态流程优化
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 修改后端 ExchangeController，接受申请后状态设为"进行中"
  - 添加"完成交换"接口，将状态改为"已完成"并扣除时间币
  - 修改前端 Exchanges.vue，显示"进行中"状态并添加完成按钮
- **Acceptance Criteria Addressed**: [AC-5]
- **Test Requirements**:
  - `human-judgement` TR-6.1: 接受申请后状态变为进行中
  - `human-judgement` TR-6.2: 点击完成按钮后状态变为已完成并扣除时间币
- **Notes**: 需要后端和前端配合修改

## [x] Task 7: 验证所有功能
- **Priority**: P0
- **Depends On**: Task 1-6
- **Description**: 
  - 测试所有新增功能
  - 验证功能正常运行
- **Acceptance Criteria Addressed**: [AC-1, AC-2, AC-3, AC-4, AC-5]
- **Test Requirements**:
  - `human-judgement` TR-7.1: 所有功能都经过测试
  - `human-judgement` TR-7.2: 功能正常运行无错误
