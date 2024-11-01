# 使用官方的 OpenJDK 17 镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 jar 文件到容器中
COPY target/*.jar app.jar

# 容器启动时运行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]

# 暴露端口
EXPOSE 8080
