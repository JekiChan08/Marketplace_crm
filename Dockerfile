FROM openjdk:17-jdk-alpine
COPY target/a.jar .
ENTRYPOINT ["java", "-Xms1024m", "-Xmx1500m", "-jar", "a.jar"]
