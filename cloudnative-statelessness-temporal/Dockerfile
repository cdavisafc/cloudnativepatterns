FROM eclipse-temurin:21
ARG jar_file
VOLUME /tmp
ADD $jar_file app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java -jar /app.jar
