FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/football-backend-app.jar .
EXPOSE 8080
CMD ["java", "-jar", "football-backend-app.jar"]



#FROM openjdk:17
##ARG JAR_FILE=target/*.jar
##COPY ${JAR_FILE} application.jar
#COPY ["target/football-backend-app.jar", "/"]
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar" , "-Xmx2048M" , "/application.jar"]