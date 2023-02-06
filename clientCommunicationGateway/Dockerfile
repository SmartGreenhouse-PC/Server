FROM openjdk:11 AS client_communication_gateway
WORKDIR /
ADD build/libs/clientCommunicationGateway-0.1.0-all.jar clientCommunicationGateway-0.1.0-all.jar
CMD java -jar clientCommunicationGateway-0.1.0-all.jar
EXPOSE 1234
EXPOSE 1235
EXPOSE 1236