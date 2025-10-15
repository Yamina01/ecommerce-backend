FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=builder /app/target/ECommercesite-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
<<<<<<< HEAD
ENTRYPOINT ["java", "-jar", "app.jar"]
=======
ENTRYPOINT ["java", "-jar", "app.jar"]
>>>>>>> 071b89bc9a8a1627535dd16fc383a9e8ce6be368
