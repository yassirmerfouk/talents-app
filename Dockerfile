FROM openjdk:17

WORKDIR /app

EXPOSE 8080

COPY target/*.jar /app/talents-app-backend.jar

CMD ["java", "-jar", "talents-app-backend.jar"]