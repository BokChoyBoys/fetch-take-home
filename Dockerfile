FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:21
COPY --from=build /home/app/target/FetchTakehome-0.0.1-SNAPSHOT.jar FetchTakehome.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/FetchTakehome.jar"]