FROM openjdk:8u282-slim

RUN mkdir /app

WORKDIR /app

COPY target/SpringBoot-Template-0.0.1.jar /app

EXPOSE 8081

CMD [ "java", "-jar", "SpringBoot-Template-0.0.1.jar" ]