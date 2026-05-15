
# 时间币和信誉分实时更新 - The Implementation Plan (Decomposed and Prioritized Task List)

## [x] Task 1: 在 Pinia Store 中添加刷新用户信息的 Action
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 在 user store 中添加 `refreshUser` 方法，用于重新获取当前用户信息并更新 store 和 localStorage
  - 该方法会调用 `/api/user/current` 接口
- **Acceptance Criteria Addressed**: [AC-1, AC-2, AC-3, AC-4]
- **Test Requirements**:
  - `programmatic` TR-1.1: user store 应该正确导入和使用 getUserInfo API
  - `human-judgement` TR-1.2: refreshUser 方法应该能成功更新 store 中的 user 状态
- **Notes**: 该方法是其他更新的基础

## [x] Task 2: 修改签到功能，签到后立即刷新用户信息
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 在 Profile.vue 中的 `handleSignIn` 函数，签到成功后调用 `refreshUser` 更新用户信息
  - 确保时间币余额和连续签到天数在前端立即显示
- **Acceptance Criteria Addressed**: [AC-1]
- **Test Requirements**:
  - `human-judgement` TR-2.1: 签到成功后，页面应该立即显示更新后的时间币
  - `human-judgement` TR-2.2: 连续签到天数应该在签到后更新
- **Notes**: 需要同时更新个人中心和导航栏等所有显示位置

## [x] Task 3: 修改发布技能功能，发布后刷新用户信息
- **Priority**: P1
- **Depends On**: Task 1
- **Description**: 
  - 在发布技能成功后调用 `refreshUser` 更新用户信息
  - 确保第一个技能发布后的时间币奖励立即显示
- **Acceptance Criteria Addressed**: [AC-2]
- **Test Requirements**:
  - `human-judgement` TR-3.1: 首次发布技能成功后，时间币应该更新
- **Notes**: 需要找到发布技能的组件，通常是 PublishSkill.vue 或类似文件

## [x] Task 4: 修改技能交换功能，完成后刷新用户信息
- **Priority**: P1
- **Depends On**: Task 1
- **Description**: 
  - 在技能交换完成或接受交换后，调用 `refreshUser` 更新信息
  - 确保时间币和信誉分变化立即显示
- **Acceptance Criteria Addressed**: [AC-3]
- **Test Requirements**:
  - `human-judgement` TR-4.1: 技能交换完成后，双方的时间币和信誉分应该更新
- **Notes**: 找到技能交换相关的组件，如 SkillExchange.vue 或 Exchanges.vue

## [x] Task 5: 修改用户信息更新功能，更新后刷新用户信息
- **Priority**: P2
- **Depends On**: Task 1
- **Description**: 
  - 在用户修改个人信息并保存后，调用 `refreshUser` 更新用户信息
  - 确保所有修改立即生效显示
- **Acceptance Criteria Addressed**: [AC-4]
- **Test Requirements**:
  - `human-judgement` TR-5.1: 修改个人信息后，页面应立即显示最新数据
- **Notes**: 修改 Profile.vue 中的 handleUpdate 函数

## [x] Task 6: 验证并测试所有更新点
- **Priority**: P0
- **Depends On**: Task 1, Task 2, Task 3, Task 4, Task 5
- **Description**: 
  - 测试所有更新场景，确保用户信息能正确刷新
  - 验证导航栏、个人中心等所有显示位置是否同步
- **Acceptance Criteria Addressed**: [AC-1, AC-2, AC-3, AC-4]
- **Test Requirements**:
  - `human-judgement` TR-6.1: 所有更新点都经过完整测试
  - `human-judgement` TR-6.2: 各页面显示一致
- **Notes**: 需要全面的端到端测试
