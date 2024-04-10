FROM openjdk:21-slim AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM openjdk:21-slim
WORKDIR .
COPY --from=build target/*.jar ticket-master-swiftie-backend.jar
ENTRYPOINT ["java", "-jar", "ticket-master-swiftie-backend.jar"]

