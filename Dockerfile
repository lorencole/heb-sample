FROM adoptopenjdk/openjdk11:jdk-11.0.13_8-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-cp","app:app/lib/*","com.l.heb.HebSampleApplication"]