FROM eclipse-temurin:21-alpine as jre-build

RUN $JAVA_HOME/bin/jlink \
 --add-modules java.base,java.desktop,java.sql,java.naming,java.management,java.net.http,jdk.jdwp.agent,jdk.crypto.ec \
 --strip-java-debug-attributes \
 --no-man-pages \
 --no-header-files \
 --compress=2 \
 --output /javaruntime

FROM alpine
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

RUN mkdir /opt/applications
COPY services/spring-server/build/libs/spring-server.jar /opt/applications/
ENV JAVA_OPTIONS=""
ENV APPLIATION="spring-server.jar"
CMD [ "sh", "-c", "java -jar ${JAVA_OPTIONS} /opt/applications/${APPLIATION}" ]
