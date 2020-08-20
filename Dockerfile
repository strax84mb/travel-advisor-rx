FROM openjdk:14-alpine
COPY target/travel-advisor-rx-*.jar travel-advisor-rx.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "travel-advisor-rx.jar"]