FROM maven:3.8.1-openjdk-11-slim AS builder
WORKDIR /resa
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:11-jre

###############
# Application #
###############

WORKDIR /resa
COPY --from=builder /resa/target/*.jar /resa/app.jar
COPY files/statics ./files/statics
COPY ./opentelemetry-javaagent-all.jar /resa/agent.jar

# Set timezone
RUN rm /etc/localtime && ln -s /usr/share/zoneinfo/Europe/Paris /etc/localtime && \
    echo "Europe/Paris" > /etc/timezone


ENTRYPOINT java $JAVA_OPTS -javaagent:agent.jar -jar app.jar