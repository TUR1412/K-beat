# 使用官方 Java 17 镜像作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将项目的 JAR 文件复制到工作目录
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# 指定容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]

# 暴露容器的端口（假设应用运行在 8080 端口）
EXPOSE 8080
