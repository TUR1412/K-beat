# 第一阶段：使用 Maven 打包项目（固定 JDK 版本，产物可复现）
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw && ./mvnw -DskipTests clean package --no-transfer-progress

# 第二阶段：将打包的 JAR 文件添加到生产镜像
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/k-beat-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
