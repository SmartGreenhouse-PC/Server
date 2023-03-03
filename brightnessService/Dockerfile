FROM openjdk:17.0.2 AS brightness_service
WORKDIR /
ADD build/libs/brightnessService-0.1.0-all.jar brightnessService-0.1.0-all.jar
CMD java -jar brightnessService-0.1.0-all.jar
EXPOSE 1883