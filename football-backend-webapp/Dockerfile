FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn package -DskipTests
# Copy static files
COPY ["target/football-backend-app.jar", "/"]
# The component ports
EXPOSE 8080
# Set working dir inside the image
WORKDIR "/"
# Run the .jar at startup
CMD ["java","-jar","football-backend-app.jar"]




#FROM openjdk:17
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} application.jar
#CMD apt-get update -y
#ENTRYPOINT ["java", "-jar", "/application.jar"]
#, "-Xmx2048M"