FROM java:8
VOLUME /tmp
COPY target/efo-1.2.jar demo.jar
RUN bash -c "touch /demo.jar"
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]



