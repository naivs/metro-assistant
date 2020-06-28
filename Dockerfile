FROM openjdk:8u232-jre-slim

ENV SPRING_PROFILES_ACTIVE=core

WORKDIR /metro-assistant
COPY target/metro-assistant-0.0.1-SNAPSHOT.jar .

ENTRYPOINT java -jar metro-assistant-0.0.1-SNAPSHOT.jar
