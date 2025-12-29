# YXB Backend - Docker 快速启动指南

## 1. 安装 Docker Desktop（如果没装）

### Windows 下载地址
https://www.docker.com/products/docker-desktop/

下载后双击安装，重启电脑。

## 2. 启动基础设施（一键）

在项目根目录 `d:\debug\yxb-backend` 下，打开 PowerShell 执行：

```powershell
docker-compose up -d
```

这会自动启动：
- **MySQL**（端口 3307，避免与你现有 3306 冲突）
- **Redis**（端口 6380，避免与你现有 6379 冲突）
- **Nacos**（端口 8849，避免与你现有 8848 冲突）

## 3. 等待服务启动完成

首次启动需要下载镜像（约 5-10 分钟），查看启动状态：

```powershell
docker-compose ps
```

当所有服务 `State` 都是 `Up` 即可。

## 4. 访问 Nacos 控制台

打开浏览器：`http://127.0.0.1:8849/nacos`

- 用户名：`nacos`
- 密码：`nacos`

创建命名空间 `yxb-backend` 并添加配置（参考主 README）。

## 5. 停止服务

```powershell
docker-compose down
```

## 6. 完全删除（包括数据）

```powershell
docker-compose down -v
```

## 默认配置

- **MySQL**：
  - 端口：3307
  - 用户名：root
  - 密码：yxb123456
  - 数据库：yxb_user（已自动创建）

- **Redis**：
  - 端口：6380
  - 无密码

- **Nacos**：
  - 端口：8849
  - 用户名/密码：nacos/nacos
