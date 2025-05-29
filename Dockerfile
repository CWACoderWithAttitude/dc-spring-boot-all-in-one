#FROM bellsoft/liberica-runtime-container:jdk-21-stream-musl as builder
FROM bellsoft/liberica-runtime-container:jdk-17-stream-musl AS builder

WORKDIR /home/app
ADD game-service /home/app/game-service
RUN cd game-service && ./mvnw -Dmaven.test.skip=true clean package


FROM bellsoft/liberica-runtime-container:jre-17-musl

WORKDIR /home/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "game-service.jar"]
COPY --from=builder /home/app/game-service/target/*.jar /home/app/game-service.jar
#CMD ["sh"]
