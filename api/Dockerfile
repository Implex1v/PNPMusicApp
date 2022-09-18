FROM openjdk:17-jdk-slim AS build
WORKDIR /app

COPY . .
RUN ./gradlew build --stacktrace

FROM openjdk:17-slim AS release
WORKDIR /app

ARG BUILD_VERSION=0.1.0
ARG GIT_COMMIT=unknown

LABEL org.opencontainers.image.version="${BUILD_VERSION}"\
      org.opencontainers.image.revision="${GIT_COMMIT}"

ENV APP_VERSION ${BUILD_VERSION}

EXPOSE 8080

COPY --from=build /app/build/libs/api-*.jar /app/api.jar
CMD ["java", "-jar", "/app/api.jar"]