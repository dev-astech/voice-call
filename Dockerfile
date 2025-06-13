FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy Maven files for dependency resolution
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn .mvn
# Make mvnw executable and build the project
RUN chmod +x mvnw && ./mvnw clean install -DskipTests --debug
# Debug: List contents of target/ to verify JAR exists
RUN ls -la target/
# Debug: List the exact JAR file to confirm its name
RUN ls -la target/voicefusion-backend-0.0.1-SNAPSHOT.jar || echo "JAR file not found"
# Copy the generated JAR
COPY target/voicefusion-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]