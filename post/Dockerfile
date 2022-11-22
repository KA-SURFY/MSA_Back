FROM openjdk:11-slim
ARG JAR_FILE=build/libs/post-0.1.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]