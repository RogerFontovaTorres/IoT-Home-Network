FROM openjdk:17-jdk-alpine
RUN mkdir project
WORKDIR project
COPY target/cloud-service-1.0-SNAPSHOT.jar /project
CMD ["java", "-jar", "/project/cloud-service-1.0-SNAPSHOT.jar"]
