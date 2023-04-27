FROM openjdk:17-alpine
MAINTAINER example.com
COPY target/github-integration-0.0.1-SNAPSHOT.jar github-integration-0.0.1.jar
ENTRYPOINT ["java","-jar","/github-integration-0.0.1.jar"]