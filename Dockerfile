FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/e-commerce.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
