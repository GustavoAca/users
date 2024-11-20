FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar /app/app.jar

ENV JAVA_OPTS="-Xmx256m -Xms128m"

RUN adduser -D usuarios

RUN apk update && \
    apk add zsh curl git ca-certificates && \
    rm -rf /var/cache/apk/*

USER usuarios

WORKDIR /home/usuarios-service

RUN sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" "" --unattended

RUN touch /home/usuarios-service/.zshrc

WORKDIR /app

EXPOSE 8080

CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --spring.profiles.active=prod"]
