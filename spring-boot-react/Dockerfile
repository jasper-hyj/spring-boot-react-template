# Use a JDK image to build the project
FROM eclipse-temurin:17-jdk-jammy as builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper files and project description
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download project dependencies
RUN ./mvnw dependency:go-offline

# Copy the application source code
COPY ./src ./src

# Build the application JAR (Skip test)
RUN ./mvnw clean package -Dmaven.test.skip=true

# Use a JRE image for the final runtime environment
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Copy the compiled JAR from the builder stage to the final image
ARG version=0.0.1-SNAPSHOT
COPY --from=builder /app/target/spring-boot-react-template-${version}.jar /app/com.example.jar

# Copy certificate
COPY cert/ /app/cert/

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/com.example.jar"]
