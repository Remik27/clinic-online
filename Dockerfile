FROM eclipse-temurin:17
COPY build/libs/*.jar clinic-online-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/clinic-online-0.0.1-SNAPSHOT.jar"]

