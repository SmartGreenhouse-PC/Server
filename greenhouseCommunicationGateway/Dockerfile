FROM openjdk:17.0.2 AS greenhouse_communication_gateway
WORKDIR /
ADD build/libs/greenhouseCommunicationGateway-0.1.0-all.jar greenhouseCommunicationGateway-0.1.0-all.jar
CMD java -jar greenhouseCommunicationGateway-0.1.0-all.jar
EXPOSE 1883