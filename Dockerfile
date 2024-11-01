# 第一阶段：使用 Maven 打包项目
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 第二阶段：将打包的 JAR 文件添加到生产镜像
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/SpuerBoot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
