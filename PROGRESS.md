# 鹦学伴（YXB）项目进度文档

> 最后更新：2024-12-22

---

## 一、项目概述

**项目名称**：鹦学伴 - 外语学习视频播放器  
**技术栈**：Java 17 + Spring Boot 3.2.1 + Spring Cloud Alibaba + MyBatis-Plus + Redis + MySQL  
**架构模式**：微服务架构，分层设计（Controller → Biz → Service → Mapper）

---

## 二、已完成模块 ✅

### 2.1 父工程与公共模块

| 模块 | 状态 | 说明 |
|------|------|------|
| `yxb-backend/pom.xml` | ✅ 完成 | 父工程，统一依赖版本管理 |
| `yxb-common-core` | ✅ 完成 | 统一响应Result、异常处理、常量、枚举、工具类 |
| `yxb-common-redis` | ✅ 完成 | Redis配置、RedisService、分布式锁服务 |
| `yxb-common-mybatis` | ✅ 完成 | MyBatis-Plus配置、自动填充、分页插件 |
| `yxb-common-security` | ✅ 完成 | Sa-Token认证配置、用户上下文拦截器 |
| `yxb-common-swagger` | ✅ 完成 | Knife4j API文档配置 |

### 2.2 API接口模块（服务间Feign调用）

| 模块 | 状态 | 说明 |
|------|------|------|
| `yxb-api-user` | ✅ 完成 | UserDTO、LoginDTO、UserFeignClient |
| `yxb-api-video` | ✅ 完成 | VideoDTO、SubtitleDTO、VideoFeignClient |
| `yxb-api-ai` | ✅ 完成 | GrammarAnalysisDTO、PronunciationScoreDTO、WordLookupDTO、AIFeignClient |
| `yxb-api-study` | ✅ 完成 | StudyProgressDTO、StudyNoteDTO、WordBookDTO |

### 2.3 网关服务

| 模块 | 状态 | 端口 | 说明 |
|------|------|------|------|
| `yxb-gateway` | ✅ 完成 | 8080 | 路由转发、统一认证、请求日志、全局异常处理 |

### 2.4 用户服务

| 模块 | 状态 | 端口 | 说明 |
|------|------|------|------|
| `yxb-user` | ✅ 完成 | 8081 | 完整实现 |

**已实现功能**：
- [x] 用户注册（手机号+验证码）
- [x] 统一登录（密码/短信/微信）- **策略模式**
- [x] Token刷新、退出登录
- [x] 用户信息查询/更新
- [x] VIP状态检查
- [x] 积分、学习时长管理
- [x] 微信绑定信息管理
- [x] 数据库Schema定义

### 2.5 视频服务

| 模块 | 状态 | 端口 | 说明 |
|------|------|------|------|
| `yxb-video` | ✅ 完成 | 8082 | 完整实现 |

**已实现功能**：
- [x] 视频列表分页查询（按语言/难度/分类筛选）
- [x] 视频详情查询
- [x] 字幕列表/指定语言字幕获取
- [x] 字幕解析器（SRT/VTT格式）- **模板方法模式**
- [x] 播放次数/收藏次数统计
- [x] 内部接口（服务间调用）
- [x] 数据库Schema定义

### 2.6 AI服务

| 模块 | 状态 | 端口 | 说明 |
|------|------|------|------|
| `yxb-ai` | ✅ 完成 | 8083 | 核心功能实现 |

**已实现功能**：
- [x] 多AI服务商配置（OpenAI/讯飞/百度）
- [x] 服务商优先级与自动降级 - **策略模式**
- [x] 语法分析（OpenAI实现）
- [x] 单词查询（带缓存）
- [x] 发音评分（讯飞实现框架）
- [x] AI问答（OpenAI实现）
- [x] 字幕生成（ASR+格式化）
- [x] 内部接口（服务间调用）

### 2.7 学习服务

| 模块 | 状态 | 端口 | 说明 |
|------|------|------|------|
| `yxb-study` | ✅ 完成 | 8084 | 完整实现 |

**已实现功能**：
- [x] 学习进度记录与查询
- [x] 单词本管理（添加/分页查询）
- [x] 待复习单词获取（艾宾浩斯遗忘曲线）
- [x] 单词掌握状态更新
- [x] 数据库Schema定义

---

## 三、未完成模块 ❌

### 3.1 微信服务

| 模块 | 状态 | 端口 | 说明 |
|------|------|------|------|
| `yxb-wechat` | ❌ 未开始 | 8085 | 微信生态集成 |

**待实现功能**：
- [ ] 小程序登录（code换取session_key）
- [ ] 公众号OAuth登录
- [ ] 微信支付（VIP购买）
- [ ] 微信消息推送
- [ ] 小程序码生成
- [ ] 分享回调统计

### 3.2 功能补充

| 功能 | 状态 | 所属服务 |
|------|------|----------|
| 视频上传/转码 | ❌ 未实现 | yxb-video |
| OSS文件存储集成 | ❌ 未实现 | yxb-video |
| 学习笔记CRUD | ❌ 未实现 | yxb-study |
| 用户收藏管理 | ❌ 未实现 | yxb-study |
| 学习统计报表 | ❌ 未实现 | yxb-study |
| 讯飞ASR完整实现 | ❌ 未实现 | yxb-ai |
| 百度AI服务实现 | ❌ 未实现 | yxb-ai |
| 有道词典API集成 | ❌ 未实现 | yxb-ai |

---

## 四、前端开发 📱

| 端 | 状态 | 技术栈 |
|------|------|--------|
| 小程序端 | ❌ 未开始 | 原生MINA / Uni-app |
| H5端（公众号） | ❌ 未开始 | Vue3 + Vant |
| 管理后台 | ❌ 未开始 | Vue3 + Element Plus |

---

## 五、DevOps与部署 🚀

| 项目 | 状态 | 说明 |
|------|------|------|
| Dockerfile | ❌ 未创建 | 各服务Docker镜像 |
| docker-compose.yml | ❌ 未创建 | 本地开发环境编排 |
| K8s部署配置 | ❌ 未创建 | 生产环境部署 |
| Jenkins Pipeline | ❌ 未创建 | CI/CD流程 |
| Prometheus监控 | ❌ 未配置 | 服务监控 |
| ELK日志系统 | ❌ 未配置 | 日志收集分析 |

---

## 六、后续开发计划

### 第一阶段：核心功能完善
1. [ ] 完成微信服务(yxb-wechat)
2. [ ] 补充学习笔记、收藏功能
3. [ ] 完善AI服务（讯飞/百度实现）
4. [ ] 视频上传与OSS集成

### 第二阶段：前端开发
1. [ ] 小程序端开发
2. [ ] 管理后台开发

### 第三阶段：部署上线
1. [ ] Docker镜像构建
2. [ ] K8s部署配置
3. [ ] CI/CD流程搭建
4. [ ] 监控告警配置

---

## 七、设计模式应用

| 模式 | 应用位置 | 说明 |
|------|----------|------|
| **策略模式** | `yxb-user` LoginStrategy | 支持密码/短信/微信多种登录方式 |
| **策略模式** | `yxb-ai` *Router类 | AI服务商自动降级切换 |
| **模板方法** | `yxb-video` SubtitleParser | SRT/VTT字幕格式解析 |
| **工厂模式** | Spring自动注入 | 策略Bean自动收集 |

---

## 八、项目结构

```
yxb-backend/
├── pom.xml
├── PROGRESS.md                      # 本文档
├── yxb-common/
│   ├── yxb-common-core/
│   ├── yxb-common-redis/
│   ├── yxb-common-mybatis/
│   ├── yxb-common-security/
│   └── yxb-common-swagger/
├── yxb-api/
│   ├── yxb-api-user/
│   ├── yxb-api-video/
│   ├── yxb-api-ai/
│   └── yxb-api-study/
├── yxb-gateway/
└── yxb-service/
    ├── yxb-user/
    ├── yxb-video/
    ├── yxb-ai/
    ├── yxb-study/
    └── yxb-wechat/                  # 待创建
```

---

## 九、更新日志

| 日期 | 更新内容 |
|------|----------|
| 2024-12-22 | 初始文档创建，后端微服务架构基本完成 |

