# Build stage
FROM maven:3.8.1-openjdk-17-slim AS build
COPY src /app/src
COPY pom.xml /app
COPY googleAuth.json app/config/googleAuth-com.quizmarkt.lifeintheuk.json

RUN mvn -f /app/pom.xml clean package

# Package stage
FROM openjdk:17
COPY --from=build /app/target/*.jar app.jar
EXPOSE 9092
ENTRYPOINT ["java","-jar","app.jar"]
