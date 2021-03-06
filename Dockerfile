FROM frolvlad/alpine-oraclejre8
VOLUME /tmp
ARG profiles=docker
ENV SPRING_PROFILES_ACTIVE=docker,$profiles
ADD target/demo-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
RUN sh -c 'touch /health'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
