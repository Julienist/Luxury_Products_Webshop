FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/luxuryproductsholding_backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","--enable-preview", "-jar", "app.jar"]
