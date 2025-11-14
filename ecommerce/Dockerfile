# ecommerce/Dockerfile (build + runtime with wait-for-es)
# ---- build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# copy pom and sources
COPY pom.xml ./
COPY src ./src

# build (skip tests to speed up during image build)
RUN mvn -B -DskipTests package

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# copy jar from build stage
COPY --from=build /app/target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

# copy wait script
COPY wait-for-es.sh /app/wait-for-es.sh
RUN chmod +x /app/wait-for-es.sh

# install curl (Debian based image), then create system user
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && addgroup --system appgroup \
 && adduser --system --ingroup appgroup --no-create-home --disabled-login appuser \
 && chown appuser:appgroup /app/app.jar /app/wait-for-es.sh

USER appuser

EXPOSE 8080

# ENTRYPOINT runs the wait script which then exec's the jar
ENTRYPOINT ["/app/wait-for-es.sh"]
 