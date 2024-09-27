FROM openjdk:24-jdk as builder

WORKDIR /app/

COPY . /app/

RUN cd /app/ && ./gradlew buildFatJar

FROM openjdk:24-slim

EXPOSE 8080

WORKDIR /app/

COPY --from=builder /app/build/libs/*.jar /app/backend.jar

CMD cd /app/ && java -jar backend.jar