FROM openjdk:11-jdk
COPY build/libs/flexrate-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]