FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy Maven files for dependency resolution
COPY pom.xml .
COPY src ./src
# Build the project to generate the JAR
RUN apt-get update && apt-get install -y maven && mvn clean install -DskipTests
# Copy the generated JAR
COPY target/voicefusion-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]