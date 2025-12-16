FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/rabbitmq-pengembalian-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8086

CMD ["java", "-jar", "app.jar"]