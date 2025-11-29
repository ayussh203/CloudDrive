# ===== STAGE 1: Build the Spring Boot jar =====
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Download dependencies first (cache-friendly)
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Now copy source and build
COPY src ./src
RUN mvn -q -DskipTests package

# ===== STAGE 2: Run the app on a slim JRE =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/clouddrive-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
