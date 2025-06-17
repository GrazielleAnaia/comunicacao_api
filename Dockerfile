FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/comunicacao_api-0.0.1-SNAPSHOT.jar /app/comunicacao_api.jar

EXPOSE 8181

CMD ["java", "-jar", "/app/comunicacao_api.jar"]

