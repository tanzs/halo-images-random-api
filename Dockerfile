# 使用官方 Maven 镜像进行构建阶段
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制本地的 settings.xml 到镜像中的 maven 配置目录
COPY settings.xml /root/.m2/settings.xml

COPY . .

# 打包 Spring Boot 应用（跳过测试，加快构建）
RUN mvn clean package -DskipTests

# ===========================================
# 生产阶段
# 使用精简版 JDK 镜像运行 jar 包
FROM eclipse-temurin:17-jdk-alpine

# 创建目录
WORKDIR /app

# 拷贝上一步构建好的 jar 包
COPY --from=builder /app/target/*.jar app.jar

# 运行 jar 应用
ENTRYPOINT ["java", "-jar", "app.jar"]
