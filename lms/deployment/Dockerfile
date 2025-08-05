FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the JAR file
COPY build/libs/MiniLibraryManagementSystem.jar MiniLibraryManagementSystem.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "MiniLibraryManagementSystem.jar"]