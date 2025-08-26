# 🌸 Halo 随机图库 API

一个基于 Spring Boot 构建的图片随机重定向服务，适用于 Halo 博客图库系统，支持 Sakura 主题随机图接口替换。可通过 Redis 缓存、MySQL 数据解析与 IP 频控提升访问效率。支持 Docker 快速部署。

本项目在自建Halo个人博客网站时，使用主题：Sakura，因主题自带随机图接口访问经常性加载不出图片，便基于Halo图库以及Sakura主题构建API接口，采用自己图库图片进行随机输出，增加访问效率。

个人博客： https://mblog.aiym.fun

原文地址：https://blog.aiym.fun:8888/archives/images-random

---

## 📦 项目结构

```
halo-random-image-api/
├── src/                         # Java 源码目录
├── pom.xml                     # Maven 项目配置
├── Dockerfile                  # Docker 构建镜像用
├── docker-compose-example.yml  # 示例 compose 启动服务配置
├── docker-example.sh           # 示例docker运行脚本命令
├── settings.xml                # Maven 阿里云加速配置
├── .gitignore
```

---

## 🚀 快速启动

### 快速使用

#### 1. 镜像拉取

通过阿里云镜像仓库拉取镜像
```bash
docker pull registry.cn-hangzhou.aliyuncs.com/aiym/halo-image-api:[镜像版本号]
```
#### 2.docker-compose启动

- 复制 docker-compose-example.yml 更改为 docker-compose-example.yml
- 调整样例中镜像名称：`image` 内容为 `registry.cn-hangzhou.aliyuncs.com/aiym/halo-image-api:[镜像版本号]`
- 启动镜像
```bash
  docker-compose up -d
```
---

### 本地使用

#### 1. 使用 Docker 构建镜像

```bash
docker build -t halo-image-api:1.0.0 .
```

#### 2. 使用 Docker Compose 启动服务

```bash
# 先复制示例
cp -r docker-compose-example.yml docker-compose.yml
# 更改相关配置后执行
docker compose up -d
```

#### 3. 使用 Docker 启动服务

```bash
docker run -d \
  --name halo-image-api \
  --network host \
  -e SERVER_PORT=8080 \
  -e MYSQL_HOST=localhost \
  -e MYSQL_PORT=3306 \
  -e MYSQL_DB=halo \
  -e MYSQL_USER=halo \
  -e MYSQL_PASSWORD=halo123 \
  -e REDIS_HOST=localhost \
  -e REDIS_PORT=6379 \
  -e REDIS_PASSWORD=halo123 \
  -e REDIS_DATABASE=0 \
  -e AUTH_KEY=testkey123 \
  -e REFERER_ALLOW_LIST=* \
  --restart=always \
  halo-image-api:1.0.0

```

如需修改环境变量，请编辑 `docker-compose.yml` 文件。

---

## ⚙️ 环境变量说明

| 变量名           | 说明                     | 示例值          |
|------------------|------------------------|--------------|
| `SERVER_PORT`    | 服务端口                   | `8080`       |
| `MYSQL_HOST`     | MySQL 主机地址             | `localhost`  |
| `MYSQL_PORT`     | MySQL 端口               | `3306`       |
| `MYSQL_DB`       | MySQL 数据库名             | `halo`       |
| `MYSQL_USER`     | MySQL 用户名              | `halo`       |
| `MYSQL_PASSWORD` | MySQL 密码               | `halo123`    |
| `REDIS_HOST`     | Redis 主机地址             | `localhost`  |
| `REDIS_PORT`     | Redis 端口               | `6379`       |
| `REDIS_PASSWORD` | Redis 密码               | `halo123`    |
| `REDIS_DATABASE` | Redis 数据库编号            | `0`          |
| `AUTH_KEY`       | API 授权 Key             | `testkey123` |
| `REFERER_ALLOW_LIST`       | 允许REFERER域（建议设置Halo站点） | `*`          |

---

## 📡 API 接口说明


### GET `/api/images-random?key=xxx&postid=xxx`

> 受保护的图片随机接口（鉴权方式）

```bash
curl http://localhost:8080/api/images-random?key=testkey123&postid=ee66de06-5241-42aa-bb64-38f040e94728
```

---

## 🧱 Maven 国内源（可选）

本项目已包含 `settings.xml` 配置，使用阿里云源加速：

```xml
<mirror>
  <id>aliyunmaven</id>
  <mirrorOf>*</mirrorOf>
  <name>阿里云公共仓库</name>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

Dockerfile 中已默认复制：

```dockerfile
COPY settings.xml /root/.m2/settings.xml
```

---

## 📜 License

MIT License - 可自由商用、修改，敬请保留引用信息。
