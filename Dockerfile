FROM adoptopenjdk/openjdk11:ubi
MAINTAINER Lingani Tshuma <tshumal@gmail.com>

RUN mkdir /opt/app
COPY ./target/bsf-digital-bank-*.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]

