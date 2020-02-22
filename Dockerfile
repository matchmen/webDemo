FROM frolvlad/alpine-oraclejre8:slim
VOLUME /tmp
ADD web-1.0-SNAPSHOT.jar /app.jar
ADD src/main/resources/application.yml /data/config/application.yml
ADD logs/web.log /data/logs/web.log
ENTRYPOINT ["java","-Dspring.config.location=data/config/application.yml","-jar","/app.jar"]
