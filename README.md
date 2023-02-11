# SmartGreenhouse Application Server
This repository contains the various micro-services of which the SmartGreenhouse Application Server is composed.

## API documentation

The different services implemented all have their own REST API, in particular the set of requests that can be made has been documented and formalized through [OpenAPI specifications](https://swagger.io/specification/) and the details can be viewed at the following link: [SmartGreenhouse Server 1.0.0](https://app.swaggerhub.com/apis/ANNAVITALI4/SmartGreenhouseServer/1.0.0)

## Deployment instructions

For deployment, first it is necessary to specifically configure the different services, in order to do so it is necessary to read the different README.md files contained within each of the sub-modules. 

### Deploy using Docker

Once you have configured all the micro-services so that they can be launched on the host, following the instructions in their appropriate README.md.
To follow this deployment option you must have installed on your host:

- Docker: [https://www.docker.com/](https://www.docker.com/)
- the 11 version of the openjdk: [https://openjdk.org/](https://openjdk.org/)
- Gradle: [https://gradle.org/](https://gradle.org/)

Once you have verified that you have everything you need to run the application, you need to move to the Server directory and launch the `gradle shadows` task, via the following command:

      ./gradlew shadowJar

At this point done if you decide to deploy the different services using `docker` and `docker-compose` you need to locate yourself in the Server directory and run the following command:

      docker compose up --build
      
Once this is done, it is necessary to wait a few moments for the different services to be instantiated. 

If you want to terminate the execution of the micro-services you need to run the command:
      
      docker compose down --rmi all

### Deploy without using Docker
Once you have configured all the micro-services so that they can be launched on the host, following the instructions in their appropriate README.md.
To follow this deployment option you must have installed on your host:

- the 11 version of the openjdk: [https://openjdk.org/](https://openjdk.org/)
- Gradle: [https://gradle.org/](https://gradle.org/)
- MongoDB: [https://www.mongodb.com/](https://www.mongodb.com/)

Before executing the application you need to import the collection that you can find on the `Server/collection` directory inside mongodb in particular you have to: create a new database with name **greenhouse** and importing the greenhouse collection identified by the `.json` contained in the directory indicated before.

Once you have verified that you have everything you need to run the application, you need to move to the Server directory and launch the `gradle shadows` task, via the following command:

      ./gradlew shadowJar

At this point, for each micro-service, the corresponding .jar file has been made, and in order to run them, you need to move to the sub-module directory then in the `/buld/libs` directory and run the command:
      
      java -jar jar_name.jar

**NOTE**: replace jar_name.jar with the filename.jar in the directory indicated.
