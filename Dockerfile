FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

#Crea un usuario sin privilegios por seguridad
RUN groupadd -r appuser && useradd -r -g appuser -d /app -s /sbin/nologin appuser

ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080
ENV JAVA_OPTS="-Xmx400m -Xss256k -XX:MaxMetaspaceSize=128m -XX:+UseSerialGC"

COPY --from=build /workspace/target/watts2crypto-backend-0.0.1-SNAPSHOT.jar /app/app.jar

#Hace que el nuevo usuario sea el dueño del jar, esto va aquí por si acaso, por si diera problemas de permisos el jar
RUN chown appuser:appuser /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --server.port=${PORT:-8080}"]
