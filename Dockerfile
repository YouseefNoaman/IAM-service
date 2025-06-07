# Build stage
FROM maven:3.9.6-eclipse-temurin-24 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Create CDS archive
FROM eclipse-temurin:24-jdk AS cds
COPY --from=builder /app/target/*.jar app.jar
RUN java -Xshare:off -XX:DumpLoadedClassList=classes.lst -jar app.jar exit && \
    java -Xshare:dump -XX:SharedClassListFile=classes.lst -XX:SharedArchiveFile=app-cds.jsa --class-path app.jar

# Final stage
FROM eclipse-temurin:24-jre-jammy
WORKDIR /app
COPY --from=cds /app/app.jar .
COPY --from=cds /app/app-cds.jsa .

ENV JAVA_OPTS="-XX:SharedArchiveFile=app-cds.jsa -Xshare:auto -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 