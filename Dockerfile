FROM openjdk:11-jdk
RUN addgroup --system spring && adduser --system spring && adduser spring spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java -Ddynamo.serviceEndpoint=${SERVICE_ENDPOINT} -Ddynamo.signingRegion=${SIGNING_REGION} -Ddynamo.accessKey=${ACCESS_KEY} -Ddynamo.secretKey=${SECRET_KEY} -jar /app.jar"]