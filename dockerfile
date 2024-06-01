# Usando uma imagem oficial do Maven para construir o projeto
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiando os arquivos de configuração do Maven
COPY pom.xml .
COPY src ./src

# Instalando as dependências e construindo o projeto
RUN mvn clean package -DskipTests

# Usando uma imagem oficial do OpenJDK para rodar o projeto
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiando o jar do estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expondo a porta que a aplicação irá rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
