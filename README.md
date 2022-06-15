# tinielogin_broker DEPLOYMENT INSTRUCTIONS

To deploy this app, build the jar first and then run using java cmd or via docker.

**Requirements**:
- [Gradle 7.4.2](https://gradle.org/releases/).
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## BUILDING JAR
- Initialise terminal window and navigate to this directory
- Execute the following commands:

      cd "Login Broker"
      ./gradlew bootJar

- Find "login_broker-0.0.1-SNAPSHOT.jar" in build/libs directory

## EXECUTING JAR USING JAVA
- Initialise terminal window and navigate to jar file location
- Set env variable for the following:
      
      DB_JDBC_URL
      CONSTANTS_SESSION_EXPIRY_DAYS
      CONSTANTS_OTP_EXPIRY_SECONDS
      CONSTANTS_TOKEN_SECRET
      CONSTANTS_OTP_GEN_URL
      CONSTANTS_USER_READ_URL
      CONSTANTS_WHATSAPP_TEMPLATE_NAME
      CONSTANTS_WHATSAPP_AUTH_KEY
      CONSTANTS_WHATSAPP_MESSAGE_URL
      
- Execute:

      java -jar login_broker-0.0.1-SNAPSHOT.jar

## EXECUTING JAR USING DOCKERFILE
- Navigate to **Login Broker** directory
- Create Dockerfile with the following content and replace placeholders:

      FROM openjdk:17.0.1-jdk
      ARG JAR_FILE=build/libs/login_broker-0.0.1-SNAPSHOT.jar
      ARG DB_JDBC_URL=<database url>
      ARG CONSTANTS_SESSION_EXPIRY_DAYS=<Token expiry duration in days>
      ARG CONSTANTS_OTP_EXPIRY_SECONDS=<OTP expiry duration in seconds>
      ARG CONSTANTS_TOKEN_SECRET=<secret for generating and validating security tokens>
      ARG CONSTANTS_OTP_GEN_URL=<URL of OTP generator service>
      ARG CONSTANTS_USER_READ_URL=<URL of User details retrieval service>
      ARG CONSTANTS_WHATSAPP_TEMPLATE_NAME=<Name of Whatsapp OTP template>
      ARG CONSTANTS_WHATSAPP_AUTH_KEY=<API key for Whatsapp messaging service>
      ARG CONSTANTS_WHATSAPP_MESSAGE_URL=<URL of Whatsapp messaging service (personalised)>
      COPY ${JAR_FILE} app.jar
      ENTRYPOINT ["java","-jar","/app.jar"]

- Run the dockerfile with the following command:

      docker build -t springio/gs-spring-boot-docker .

