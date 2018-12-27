FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD musixise-box-web/target/musixise-box-web-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 /app.jar" ]