FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.7.7/applicationinsights-agent-3.7.7.jar applicationinsights-agent.jar
ENTRYPOINT ["java", "-javaagent:/applicationinsights-agent.jar", "-jar", "/app.jar"]