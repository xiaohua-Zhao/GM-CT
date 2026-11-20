# GM-CT 成泰服装管理系统

Spring Boot + MyBatis-Plus + Vue 3 的前后端分离项目。

## 目录

- `server/`：Spring Boot 3.5.3 + MyBatis-Plus 3.5.7 后端
- `web/`：Vue 3 + Vite + Element Plus 前端
- `logs/`：运行日志目录，按天滚动，保留 30 天
- `attachments/`：附件存储目录
- `database/`：数据库初始化脚本

## 后端启动

```powershell
cd D:\DevelopSoftware\code\coding\code\GM-CT\server
D:\DevelopSoftware\maven\apache-maven-3.6.3\bin\mvn.cmd spring-boot:run
```

后端地址：`http://localhost:8080`

健康检查：`http://localhost:8080/api/health`

## 前端启动

```powershell
cd D:\DevelopSoftware\code\coding\code\GM-CT\web
npm install
npm run dev
```

前端地址：`http://localhost:5173`

## 日志

日志文件：

```text
logs/chengtai-fashion-mgmt.log
```

按天滚动，保留最近 30 天，单文件超过 50MB 滚动。

## 附件

附件保存到：

```text
attachments/yyyy/MM/dd/
```

上传接口：`POST /api/files/upload`

## 数据库

数据库名：`user`

本地配置位于 `server/src/main/resources/application-local.yml`，该文件不会提交到 Git。
