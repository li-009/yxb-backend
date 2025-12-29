# 鹦学伴 - 外语学习视频助手 (YXB Backend)

## 项目概览

**鹦学伴**是一款以视频为核心载体，结合AI智能辅助与微信生态的外语学习应用。提供"看-学-练-问-分享"全链路学习服务，支持视频播放、字幕解析、影子跟读、AI语法讲解、发音评分等功能。

**核心特点**：
- 视频播放 + 智能字幕解析
- AI辅助学习（字幕生成、语法讲解、发音评分）
- 影子跟读与实时评分
- 屏幕查词与单词本管理
- 微信生态深度集成（登录、分享、社群）
- 学习数据同步与个性化推荐

**适用平台**：iOS、Android、微信小程序、微信公众号

**技术栈**：
- Java 17
- Spring Boot 3.x
- Spring Cloud Alibaba (Nacos + Gateway)
- MyBatis-Plus
- Redis
- MySQL 8.0
- Sa-Token (权限认证)
- WxJava (微信SDK)

---

## 已完成功能模块

### ✅ 后端微服务架构
- [x] `yxb-gateway` - API 网关（端口 8080）
- [x] `yxb-user` - 用户服务（端口 8081）
- [x] `yxb-wechat` - 微信服务（端口 8085）
- [ ] `yxb-video` - 视频管理服务（待开发）
- [ ] `yxb-subtitle` - 字幕解析服务（待开发）
- [ ] `yxb-ai` - AI接口整合服务（待开发）
- [ ] `yxb-learning` - 学习数据管理服务（待开发）

### ✅ 公共模块
- [x] `yxb-common-core` - 核心工具类、统一响应、异常处理
- [x] `yxb-common-redis` - Redis 缓存配置
- [x] `yxb-common-mybatis` - MyBatis-Plus 配置
- [x] `yxb-common-satoken` - Sa-Token 认证配置

### ✅ 微信小程序集成
- [x] 微信小程序登录（`code2session`）
- [x] 获取手机号（`getPhoneNumber`）
- [x] 微信支付下单
- [x] 微信支付回调处理
- [x] 网关路由配置（`/api/wechat/**`）
- [x] 白名单配置（登录、支付回调免鉴权）

### ✅ 数据库设计
- [x] 完整数据库表结构设计（30张表）
- [x] 用户模块（用户信息、VIP状态）
- [x] 视频模块（视频信息、下载记录）
- [x] 字幕模块（字幕文件、字幕内容）
- [x] 学习记录模块（播放记录、跟读记录、笔记）
- [x] 单词本模块（单词管理、复习计划）
- [x] AI辅助功能（语法讲解、AI问答）
- [x] 运营模块（打卡、积分、推荐）
- [x] 微信生态模块（分享、社群、任务）
- [x] 在线资源库（分类、资源）

### ✅ 前端应用
#### 小程序端（UniApp）
- [x] UniApp 小程序框架搭建
- [x] 登录页面（微信授权、手机号）
- [x] 统一请求封装
- [x] 网关接口对接
- [ ] 视频播放页面（待开发）
- [ ] 学习功能界面（待开发）

#### 后台管理端（Vue3 + Element Plus）
- [x] Vue3 + Element Plus 项目搭建
- [x] 登录页面（账号密码）
- [x] 布局组件（侧边栏、顶栏）
- [x] 数据看板（统计卡片、ECharts图表）
- [x] 用户管理（列表、查询、禁用）
- [x] 视频管理（列表、上传、编辑）
- [x] 商户管理（列表、新增、编辑）
- [x] 订单管理（列表、详情）
- [x] 系统设置（基本设置、VIP设置）

#### 商户中心（Vue3 + Element Plus）
- [x] Vue3 + Element Plus 项目搭建
- [x] 登录页面（商户账号）
- [x] 工作台（数据概览、收益趋势）
- [x] 视频管理（我的视频、上传）
- [x] 收益管理（收益明细）
- [x] 提现管理（申请提现、提现记录）
- [x] 账户设置（商户信息、银行卡）

### ✅ 用户认证体系
- [x] 手机号注册
- [x] 账号密码登录
- [x] 微信授权登录（自动注册）
- [x] Token 刷新/退出
- [x] Sa-Token 集成

---

## 项目结构

```
yxb-backend/
├── yxb-gateway/              # API 网关
├── yxb-service/
│   ├── yxb-user/            # 用户服务
│   ├── yxb-wechat/          # 微信服务
│   ├── yxb-ai/              # AI 服务（预留）
│   ├── yxb-video/           # 视频服务（预留）
│   └── yxb-study/           # 学习服务（预留）
├── yxb-common/
│   ├── yxb-common-core/     # 核心工具
│   ├── yxb-common-redis/    # Redis 配置
│   ├── yxb-common-mybatis/  # MyBatis-Plus 配置
│   └── yxb-common-satoken/  # Sa-Token 配置
├── yxb-miniprogram/         # 前端小程序（UniApp）
├── yxb-admin/               # 后台管理端（Vue3 + Element Plus）
├── yxb-merchant/            # 商户中心（Vue3 + Element Plus）
├── docker-compose.yml       # Docker 基础设施
└── README-Docker.md         # Docker 使用指南
```

---

## 环境配置

### 开发环境要求
- **JDK 17**（已配置）
- **Maven 3.8+**
- **Docker Desktop**（用于基础设施）
- **Node.js 16+**（前端项目）
- **npm 或 pnpm**（包管理器）

### 基础设施（Docker 一键启动）

已配置 Docker Compose，端口与现有服务隔离：

| 服务  | Docker 端口 | 本地访问地址 | 用户名/密码 |
|-------|------------|--------------|-------------|
| MySQL | 3307 | 127.0.0.1:3307 | root/yxb123456 |
| Redis | 6380 | 127.0.0.1:6380 | 无密码 |
| Nacos | 8850 | http://127.0.0.1:8850/nacos | nacos/nacos |

**启动命令**：
```powershell
cd D:\debug\yxb-backend
docker-compose up -d
```

详见 `README-Docker.md`

---

## Nacos 配置

### 1. 命名空间
- **命名空间 ID**：`yxb-backend`
- **用途**：隔离本项目与其他项目的服务

### 2. 配置文件（需要在Nacos控制台创建）

在Nacos控制台 http://127.0.0.1:8850/nacos 的 `yxb-backend` 命名空间下创建以下配置：

#### 共享配置
- **Data ID**：`application-dev.yml`
- **Group**：`DEFAULT_GROUP`
- **内容**：MySQL、Redis、MyBatis-Plus、Sa-Token 等通用配置

#### 服务配置
- `yxb-user-dev.yml` - 用户服务配置（微信小程序配置）
- `yxb-wechat-dev.yml` - 微信服务配置（微信支付配置）
- `yxb-gateway-dev.yml` - 网关配置（路由、CORS）
- `yxb-video-dev.yml` - 视频服务配置
- `yxb-ai-dev.yml` - AI服务配置
- `yxb-study-dev.yml` - 学习服务配置

详见启动文档中的Nacos配置章节

---

## 启动步骤

### 1. 启动基础设施
```powershell
cd D:\debug\yxb-backend
docker-compose up -d
```

### 2. 验证 Docker 服务状态
```powershell
docker ps
# 应该看到 yxb-mysql, yxb-redis, yxb-nacos 三个容器运行中
```

### 3. 初始化数据库
```powershell
# 登录 MySQL
mysql -h127.0.0.1 -P3307 -uroot -pyxb123456

# 创建微服务数据库
CREATE DATABASE yxb_user DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE yxb_wechat DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE yxb_video DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE yxb_ai DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE yxb_study DEFAULT CHARACTER SET utf8mb4;
```

### 4. 配置 Nacos
访问 Nacos 控制台：http://127.0.0.1:8850/nacos (nacos/nacos)

- 创建命名空间 `yxb-backend`
- 在该命名空间下创建所需的配置文件（见 Nacos 配置章节）

### 5. 启动后端微服务（在 IDEA 中按顺序启动）
1. `YxbUserApplication` - 用户服务（8081）
2. `YxbWechatApplication` - 微信服务（8085）
3. `YxbGatewayApplication` - 网关服务（8080）

### 6. 启动前端项目

#### 后台管理端
```powershell
cd D:\debug\yxb-backend\yxb-admin
npm install
npm run dev
```
访问：http://localhost:3000  
测试账号：admin / admin123

#### 商户中心
```powershell
cd D:\debug\yxb-backend\yxb-merchant
npm install
npm run dev
```
访问：http://localhost:5174  
测试账号：merchant / 123456

#### 小程序端
在 HBuilderX 中打开 `yxb-miniprogram` 目录，运行到微信开发者工具

### 7. 验证服务注册
访问 Nacos 控制台：http://127.0.0.1:8850/nacos

切换到 `yxb-backend` 命名空间，查看服务列表：
- `yxb-gateway` (8080)
- `yxb-user` (8081)
- `yxb-wechat` (8085)

---

## 接口文档

### Swagger/Knife4j（本地）
- 用户服务：http://127.0.0.1:8081/doc.html
- 微信服务：http://127.0.0.1:8085/doc.html

### 核心接口

#### 用户服务（通过网关访问）
- `POST /api/auth/register` - 手机号注册
- `POST /api/auth/login` - 统一登录（支持账号密码/微信）
- `POST /api/auth/refresh` - 刷新 Token
- `POST /api/auth/logout` - 退出登录

#### 微信服务（通过网关访问）
- `POST /api/wechat/miniapp/login` - 小程序登录（免鉴权）
- `POST /api/wechat/miniapp/phone` - 获取手机号（免鉴权）
- `POST /api/wechat/pay/create` - 创建支付订单
- `POST /api/wechat/pay/notify` - 支付回调（免鉴权）
- `GET /api/wechat/pay/query/{orderNo}` - 查询订单

---

## 数据库设计

### 完整数据库表结构
**设计文档**：`database-design.sql`

**数据库名**：`yxb_main`

**包含10个核心模块，共30张表**：

1. **用户模块** (1张表)
   - `yxb_user` - 用户基本信息、学习统计、VIP状态

2. **视频模块** (2张表)
   - `yxb_video` - 视频信息、来源、分类、统计
   - `yxb_video_download` - 离线下载记录

3. **字幕模块** (2张表)
   - `yxb_subtitle` - 字幕文件信息
   - `yxb_subtitle_content` - 字幕逐句内容

4. **学习记录模块** (3张表)
   - `yxb_learning_record` - 视频学习进度
   - `yxb_follow_reading` - 跟读录音与评分
   - `yxb_note` - 笔记与收藏

5. **单词本模块** (2张表)
   - `yxb_vocabulary` - 单词本
   - `yxb_vocabulary_review` - 复习记录（艾宾浩斯曲线）

6. **AI辅助功能** (2张表)
   - `yxb_grammar_explanation` - 语法讲解记录
   - `yxb_ai_question` - AI问答记录

7. **运营模块** (3张表)
   - `yxb_checkin` - 学习打卡
   - `yxb_points` - 积分记录
   - `yxb_daily_recommendation` - 每日推荐

8. **微信生态** (6张表)
   - `yxb_wechat_share` - 分享记录
   - `yxb_study_group` - 学习社群
   - `yxb_study_group_member` - 社群成员
   - `yxb_study_task` - 学习任务
   - `yxb_task_completion` - 任务完成记录

9. **在线资源库** (2张表)
   - `yxb_resource_category` - 资源分类
   - `yxb_online_resource` - 在线资源

10. **系统配置** (1张表)
    - `yxb_system_config` - 系统配置

### 数据库初始化
```bash
# 登录 MySQL（Docker）
mysql -h127.0.0.1 -P3307 -uroot -pyxb123456

# 创建数据库
CREATE DATABASE yxb_main DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入表结构
USE yxb_main;
SOURCE database-design.sql;
```

---

## 前端小程序开发

### 项目位置
`yxb-miniprogram/`

### 技术栈
- UniApp
- 微信小程序

### 配置后端地址
`yxb-miniprogram/config.js`：
```javascript
export const config = {
  baseUrl: 'http://127.0.0.1:8080'  // 网关地址
}
```

### 测试页面
`pages/index/index.vue` - 包含登录/获取手机号测试

---

## 待完成功能

### 🔄 第一阶段：基础版开发（1-4个月）
- [x] 后端微服务架构搭建
- [x] 数据库表结构设计
- [ ] Docker 基础设施启动
- [ ] 视频管理服务开发（上传、存储、播放控制）
- [ ] 字幕解析服务开发（解析、切换、互动）
- [ ] 微信小程序基础界面（首页、播放页）
- [ ] 微信登录与分享功能完善

### 📋 第二阶段：进阶版开发（5-8个月）
- [ ] AI接口整合服务（字幕生成、语法讲解）
- [ ] 影子跟读功能（录音、评分、纠错）
- [ ] 屏幕查词功能（单词本、复习计划）
- [ ] 学习数据管理（笔记、收藏、进度同步）
- [ ] 微信扫码学习功能
- [ ] 微信AI问答功能
- [ ] iOS/Android原生应用开发

### 🚀 第三阶段：完整版开发（9-12个月）
- [ ] 在线资源库建设
- [ ] 运营功能（打卡、积分、推荐）
- [ ] 微信社群联动功能
- [ ] 微信支付集成
- [ ] 管理后台（若依框架）
- [ ] 性能优化与全面测试

---

## 开发规范

### 代码结构
- **Controller** → **Biz** → **Service** → **Mapper**
- Biz 层：业务逻辑
- Service 层：细粒度 DB 操作

### 异常处理
- 使用 `BizException` 抛出业务异常
- 全局异常处理器：`GlobalExceptionHandler`

### 统一响应
- 使用 `Result<T>` 封装响应
- `Result.success(data)` - 成功
- `Result.fail(msg)` - 失败

---

## 常用命令

### Maven 编译
```powershell
mvn clean compile -DskipTests
```

### Docker 管理
```powershell
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f nacos

# 停止所有服务
docker-compose down

# 完全删除（包括数据）
docker-compose down -v
```

---

## 常见问题

### Q: 连不上 Nacos？
**A**: 确认 Docker 已启动，且 Nacos 在 `yxb-backend` 命名空间

### Q: 服务启动报数据库连接失败？
**A**: 
1. 确认 MySQL 容器已启动：`docker-compose ps`
2. 手动创建数据库：`yxb_user`、`yxb_wechat`
3. 检查密码是否为 `yxb123456`

### Q: 编译报错找不到符号？
**A**: 
1. IDEA 刷新 Maven：右键项目 → Maven → Reload Project
2. 确认 JDK 为 17：File → Project Structure → SDK

---

## 项目进度

- ✅ 后端架构搭建（100%）
- ✅ 微信服务开发（100%）
- ✅ Docker 环境配置（100%）
- ✅ Nacos 配置中心集成（100%）
- ✅ 前端小程序框架（100%）
- ✅ 后台管理端开发（100%）
- ✅ 商户中心开发（100%）
- 🔄 服务联调测试（30%）
- ⏳ 视频服务开发（0%）

---

## 更新日志

### 2025-12-23
- ✅ Docker基础设施启动完成（MySQL 3307、Redis 6380、Nacos 8850）
- ✅ Nacos数据库初始化（nacos库12张表）
- ✅ 创建命名空间 yxb-backend
- ✅ 创建所有微服务数据库（yxb_user、yxb_wechat、yxb_video、yxb_ai、yxb_study）
- ✅ 在Nacos创建7个配置文件（共享配置+服务配置）
- ✅ 修改所有微服务application.yml（端口8850、命名空间、config.import）
- ✅ 成功启动后端微服务（yxb-user、yxb-wechat、yxb-gateway）
- ✅ 创建后台管理端（Vue3 + Element Plus）
  - 登录页面（紫色渐变、账号密码）
  - 布局组件（侧边栏、顶栏、面包屑）
  - 数据看板（统计卡片、ECharts图表）
  - 用户管理、视频管理、商户管理、订单管理、系统设置
- ✅ 创建商户中心（Vue3 + Element Plus）
  - 登录页面（绿色渐变、商户账号）
  - 工作台、视频管理、收益管理、提现管理、账户设置
- ✅ 前端项目启动成功（管理端3000、商户端5174）
- ✅ 更新README文档，完善启动流程和配置说明

### 2025-12-22
- ✅ 明确项目定位："鹦学伴"外语学习视频助手
- ✅ 完成完整数据库表结构设计（30张表，10个模块）
- ✅ 设计核心功能：视频播放、字幕解析、AI辅助、跟读评分
- ✅ 规划微信生态集成：登录、分享、社群、任务
- ✅ 创建微信服务模块（yxb-wechat）
- ✅ 实现小程序登录/手机号获取
- ✅ 实现微信支付下单/回调
- ✅ 网关路由配置及白名单
- ✅ 创建 UniApp 小程序项目
- ✅ 配置 Docker Compose 基础设施
- ✅ 配置 Nacos namespace 隔离
- ✅ JDK 17 环境配置
- ✅ 编译验证通过

---

## 联系方式

如有问题，请提交 Issue 或联系项目负责人。
