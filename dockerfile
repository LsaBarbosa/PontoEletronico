# Etapa de build
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml /app/
RUN mvn dependency:go-offline -B
COPY src /app/src
RUN mvn clean package -DskipTests && mvn clean

# Etapa de runtime
FROM openjdk:17-alpine3.13
WORKDIR /app
COPY --from=build /app/target/*.jar /app/PontoEletronico-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
