# 第一步：使用 Maven 镜像来构建项目
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app

# 复制父项目的 POM 文件到 Docker 镜像
COPY ../TireShop-MircroService/pom.xml /app/TireShop-MircroService/pom.xml

# 复制子项目 order-service 的 POM 文件和源代码到 Docker 镜像
COPY order-service/pom.xml /app/pom.xml
COPY order-service/src /app/src

# 使用 Maven 打包 order-service 项目
RUN mvn clean package

# 第二步：构建运行时环境，使用 OpenJDK 运行 Spring Boot 应用
FROM openjdk:17-jdk-slim
WORKDIR /app

# 复制第一阶段构建好的 JAR 文件
COPY --from=build /app/target/order-service.jar /app/order-service.jar

# 暴露 Spring Boot 默认的 8080 端口
EXPOSE 8080

# 运行 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "order-service.jar"]
