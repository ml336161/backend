# 社区技能互助平台

一个基于 Spring Boot + Vue3 的社区技能互助平台，用户可以发布、浏览、交换技能，使用时间币进行交易。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 5.7
- **ORM**: MyBatis
- **安全**: JWT
- **密码加密**: Spring Security Crypto

### 前端
- **框架**: Vue 3.5.x
- **路由**: Vue Router 4.x
- **状态管理**: Pinia
- **UI组件**: Element Plus
- **图表**: ECharts
- **构建工具**: Vite 8.x

## 功能模块

### 用户模块
- 用户注册（含用户名、密码、确认密码、昵称、出生日期）
- 用户登录
- 个人信息编辑（头像、昵称、出生日期、邮箱）
- 密码修改

### 首页（技能大厅）
- 热门推荐（按收藏+点赞+浏览综合排序）
- 最新发布（按时间倒序）
- 分类筛选
- 关键词搜索

### 技能模块
- 技能发布
- 技能详情
- 技能收藏
- 技能点赞

### 互动模块
- 评论功能
- 删除评论

### 社交模块
- 好友申请/处理
- 好友列表
- 私信发送/接收

### 个人中心
- 个人信息展示（头像、用户名、出生日期、信用分）
- 时间币余额与签到
- 我的收藏列表
- 我的点赞列表
- 我的好友
- 我的技能（可编辑/删除）
- 技能交换记录

### 时间币体系
- 注册赠送（3个时间币）
- 每日签到（+1时间币）
- 交换收支

### 技能交换
- 申请交换
- 接受/完成交换

### 信用雷达图
- 五维雷达图展示（技能数、交换数、好评数、登录天数、时间币）

### 管理员
- 用户管理
- 举报处理
- 技能管理
- 数据看板
- 操作日志

## 项目结构

```
skill-exchange-backend/
└── skill-exchange/
    ├── src/main/java/com/skillexchange/
    │   ├── controller/     # REST API 控制层
    │   ├── service/        # 业务逻辑层
    │   ├── mapper/         # 数据访问层
    │   ├── entity/         # 数据库实体
    │   ├── dto/            # 数据传输对象
    │   ├── vo/             # 视图对象
    │   ├── config/         # 配置类
    │   ├── interceptor/    # 拦截器
    │   ├── exception/      # 异常处理
    │   └── utils/          # 工具类
    └── src/main/resources/
        ├── mapper/         # MyBatis XML 文件
        └── application.yml # 应用配置

skill-exchange-frontend/
└── src/
    ├── views/             # 页面组件
    ├── components/        # 通用组件
    ├── api/               # API 接口封装
    ├── router/            # 路由配置
    ├── store/             # 状态管理
    ├── utils/             # 工具函数
    └── assets/            # 静态资源
```

## 运行环境

- Java 1.8+
- MySQL 5.7+
- Node.js 18+

## 快速开始

### 1. 数据库配置

创建数据库并执行初始化脚本：

```sql
-- 创建数据库
CREATE DATABASE skill_exchange DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行初始化脚本
source init.sql;
```

### 2. 后端运行

```bash
cd skill-exchange-backend/skill-exchange
mvn spring-boot:run
```

服务将在 `http://localhost:8080` 启动。

### 3. 前端运行

```bash
cd skill-exchange-frontend
npm install
npm run dev
```

前端将在 `http://localhost:5173` 启动。

## API 接口示例

### 用户相关

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/user/register` | POST | 用户注册 |
| `/api/user/login` | POST | 用户登录 |
| `/api/user/current` | GET | 获取当前用户信息 |
| `/api/user/update` | PUT | 更新用户信息 |
| `/api/user/profile-stats` | GET | 获取个人中心统计数据 |

### 技能相关

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/skills` | GET | 获取技能列表 |
| `/api/skills/hot` | GET | 获取热门技能 |
| `/api/skills/latest` | GET | 获取最新发布技能 |
| `/api/skills/{id}` | GET | 获取技能详情 |
| `/api/skills` | POST | 发布技能 |
| `/api/skills/{id}/like` | POST | 点赞/取消点赞 |
| `/api/skills/{id}/collect` | POST | 收藏/取消收藏 |
| `/api/skills/collects` | GET | 获取我的收藏列表 |
| `/api/skills/likes` | GET | 获取我的点赞列表 |

## 测试账号

### 管理员账号
- 用户名: `admin`
- 密码: `123456`

### 普通用户
- 用户名: `zhangsan`
- 密码: `123456`

- 用户名: `lisi`
- 密码: `123456`

- 用户名: `wangwu`
- 密码: `123456`

- 用户名: `zhaoliu`
- 密码: `123456`

## 开发说明

### 代码规范
- 后端采用 Spring Boot 标准分层架构
- 前端采用 Vue 3 Composition API
- 使用 Lombok 简化实体类代码
- 统一响应格式使用 `Result<T>`

### 安全
- 使用 JWT 进行身份认证
- 密码明文存储（测试环境）
- 接口权限通过 JWT token 验证

### 配置说明
后端配置文件 `application.yml` 中：
- 数据库用户名: `root`
- 数据库密码: `ml336161`
- 文件上传路径: `C:/data/upload/`

## License

MIT License