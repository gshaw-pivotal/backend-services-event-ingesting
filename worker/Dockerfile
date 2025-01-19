FROM amazoncorretto:23
LABEL authors="Gavin"
COPY target/worker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8801
ENTRYPOINT ["java","-jar","/app.jar"]