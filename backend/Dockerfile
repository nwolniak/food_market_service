FROM eclipse-temurin:20-jdk-alpine as base

WORKDIR /backend

COPY .mvn/ .mvn
COPY pom.xml ./
COPY mvnw ./
RUN ./mvnw dependency:resolve

COPY src ./src

FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=mysql"]