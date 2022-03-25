FROM openjdk:11-slim
WORKDIR /opt
ENV PORT 9090
EXPOSE 9090
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar