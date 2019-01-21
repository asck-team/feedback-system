FROM arm32v7/openjdk:11-slim
MAINTAINER Constantin Krüger (Constantin.Krueger@badenia.de)

VOLUME /tmp
ARG JAR_FILE
ENV QRCODE_HOST 127.0.0.1
ENV QRCODE_PORT 8080 
ENV DB_TYPE postgres
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dqr.code.host=${QRCODE_HOST}","-Dqr.code.port=${QRCODE_PORT}","-Dspring.profiles.active=${DB_TYPE}","-jar","/app.jar"]
