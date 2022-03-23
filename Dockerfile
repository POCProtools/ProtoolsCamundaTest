FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD target/ProtoolsTest-0.0.1-SNAPSHOT.jar ProtoolsTest-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ProtoolsTest-0.0.1-SNAPSHOT.jar"]