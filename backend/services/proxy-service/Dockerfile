FROM openjdk:17-slim

WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src

RUN ./gradlew build -x test

EXPOSE 8083
CMD ["java", "-jar", "build/libs/proxy-service.jar"]
