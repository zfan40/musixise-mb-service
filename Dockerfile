FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD musixise-box-web/target/musixise-box-web-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
RUN apk --no-cache add tzdata  && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
