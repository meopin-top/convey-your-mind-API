FROM adoptopenjdk/openjdk11
COPY build/libs/*-SNAPSHOT.jar app.jar
ARG SPRING_PROFILES_ACTIVE
ENTRYPOINT ["java","-Dspring.profiles.active=${USE_PROFILE}","-jar","app.jar"]