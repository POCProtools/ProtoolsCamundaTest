FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD target/ProtoolsTest-1.0.0-SNAPSHOT.jar ProtoolsTest-1.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ProtoolsTest-1.0.0-SNAPSHOT.jar"]