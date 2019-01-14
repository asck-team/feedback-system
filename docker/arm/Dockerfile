FROM resin/raspberry-pi-openjdk
MAINTAINER Constantin Krüger (Constantin.Krueger@badenia.de)
COPY qemu-arm-static /usr/bin/qemu-arm-static
VOLUME /tmp
ARG JAR_FILE
ENV QRCODE_HOST 127.0.0.1
ENV QRCODE_PORT 8080 
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dqr.code.host=${QRCODE_HOST}","-Dqr.code.port=${QRCODE_PORT}","-jar","/app.jar"]
