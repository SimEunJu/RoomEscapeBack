FROM openjdk:11
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
COPY /info/ssl/ /info/ssl/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]