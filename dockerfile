FROM openjdk:11-jdk-slim
COPY . /app
WORKDIR /app
RUN ./mvnw clean package -DskipTests
ENTRYPOINT ["java", "-jar", "target/PontoEletronico-0.0.1-SNAPSHOT.jar"]
