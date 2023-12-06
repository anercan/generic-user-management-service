# Use Maven for building and OpenJDK for runtime
FROM maven:3.8.3-openjdk-11-slim AS build

WORKDIR /app

# Copy only the necessary files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package

# Final image with only JRE
FROM adoptopenjdk:11-jre-hotspot

WORKDIR /app

# Copy the JAR file into the container
COPY --from=build /app/target/user-management-service.jar /app/app.jar

# Expose the port on which your Java application will run
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]