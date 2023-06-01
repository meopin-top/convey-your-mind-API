FROM adoptopenjdk/openjdk11
COPY build/libs/*-SNAPSHOT.jar app.jar
ENV	USE_PROFILE dev
ENTRYPOINT ["java","-Dspring.profiles.active=${USE_PROFILE}","-jar","app.jar"]