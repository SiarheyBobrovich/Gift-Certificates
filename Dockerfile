FROM openjdk:17

COPY build/libs/certificate-2.0.0.jar /app/certificate-2.0.0.jar

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "/app/certificate-2.0.0.jar"]