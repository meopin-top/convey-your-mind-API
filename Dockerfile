FROM adoptopenjdk/openjdk11
COPY build/libs/*-SNAPSHOT.jar app.jar
ARG SPRING_PROFILES_ACTIVE
ENTRYPOINT ["java","-jar","app.jar"]