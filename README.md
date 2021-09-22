# BSF Digital Bank Technical Assessment

Backend Service that exposes 2 REST APIs
1. To accept money transfers to other accounts. Money transfers should persist new balance of accounts
2. For creating an account and getting the account details. It disregards currencies at this time

## Getting Started

* Using either an Eclipse IDE or IntelliJ, import the source code as an Existing Maven Project. 

## Prerequisites

* Install [Java SDK](https://openjdk.java.net/)
  * [Java SDK 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Required only for JUnit tests using CodeSV mock services.
* Install [Apache Maven](https://maven.apache.org/install.html)
* Install [Eclipse IDE](https://www.eclipse.org/ide/), [Spring Tools Suite](https://spring.io/tools) or [IntelliJ](https://www.jetbrains.com/idea/)

## Executing Tests

* JUnit Tests - Execute ```mvn clean test```

## Packaging

* To create a deployment package, execute ```mvn clean package```
* To create a new Docker image, use the [Dockerfile](Dockerfile)  by executing ```docker build -t bsf-digital-bank:1.0.0 .```

## Deployment

Digital Bank can be deployed as a single standalone application service or be deployed and configured to integrate with Digital Credit.

* Deploy as a Jar file ```java -jar target\bsf-digital-bank-1.0.0.jar```
* Deploy as Docker container ```docker run -it bsf-digital-bank:1.0.0```
  
## API Documentation

* Swagger UI @ http://{hostname}:{port}/swagger-ui.html
* Localhost: http://localhost:8080/swagger-ui.html

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - Application Framework
* [Swagger](https://swagger.io/) - API Documentation
* [Apache Maven](https://maven.apache.org/) - Software Project Management
* [H2 Database Engine](https://www.h2database.com/html/main.html) - In-Memory Database
* [JUnit 5](https://junit.org/junit5/) - Unit Testing Framework
* [BlazeMeter](https://www.blazemeter.com/) - Functional / Performance API & UI Testing Services


## Authors

[Lingani Tshuma](https://github.com/tshumal)

