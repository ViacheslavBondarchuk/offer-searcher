FROM gradle:7.5.1-jdk17-alpine AS builder
WORKDIR /app
COPY build.gradle gradle.properties settings.gradle ./
COPY src/ src/
RUN gradle --no-daemon build --stacktrace

FROM openjdk:17-alpine
WORKDIR /app
RUN apk --no-cache add curl
COPY --from=builder /app/build/libs/*.jar /offer-searcher.jar
ENV PORT 8080
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=default", "/offer-searcher.jar"]
