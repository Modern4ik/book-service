FROM openjdk:17-jdk-slim

WORKDIR /book-service

COPY target/booksAPI-0.0.1-SNAPSHOT.jar book-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "book-service.jar"]