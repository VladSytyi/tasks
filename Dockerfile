FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build/libs/*.jar tasks.jar
EXPOSE 8080
LABEL maintainer="Vladislav Sytyi"
ENTRYPOINT ["java","-jar","/tasks.jar"]
