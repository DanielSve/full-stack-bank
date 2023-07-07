FROM maven:3.8.1-openjdk-17-slim as builder
COPY . .
RUN mvn clean package
FROM --platform=linux/amd64/v8 openjdk:17
WORKDIR app
COPY --from=builder /target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]