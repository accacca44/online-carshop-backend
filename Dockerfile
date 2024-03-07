#
# docker build -t spring-gradle-multistage:0.0.1 .
# docker run -it --rm -p 8080:8080 spring-gradle-multistage:0.0.1
#

# 1. fázis: kompilálás s distributable előkészítése
FROM gradle:8.3.0-jdk17-alpine AS build-env
ARG SPRING_PROFILE
WORKDIR /app
COPY . ./
RUN SPRING_PROFILES_ACTIVE=$SPRING_PROFILE gradle bootJar --no-daemon

# 2. fázis: distributable átmásolása a Gradle konténerből egy egyszerű JRE-sre
FROM eclipse-temurin:17-jre-alpine
ARG SPRING_PROFILE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILE
WORKDIR /usr/app
COPY --from=build-env /app/build/libs/keim2152-idde.jar ./
CMD java -jar keim2152-idde.jar
