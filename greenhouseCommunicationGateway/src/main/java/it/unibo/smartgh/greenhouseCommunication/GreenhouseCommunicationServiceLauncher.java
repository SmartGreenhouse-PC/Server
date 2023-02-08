package it.unibo.smartgh.greenhouseCommunication;

import io.vertx.core.Vertx;
import it.unibo.smartgh.greenhouseCommunication.api.http.GreenhouseCommunicationHTTPModel;
import it.unibo.smartgh.greenhouseCommunication.api.mqtt.GreenhouseCommunicationMQTTModel;
import it.unibo.smartgh.greenhouseCommunication.service.GreenhouseCommunicationService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This is the entry point to execute the service.
 */
public class GreenhouseCommunicationServiceLauncher {

    public static void  main(String[] args){
        Vertx vertx = Vertx.vertx();
        try {
            InputStream is = GreenhouseCommunicationServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String httpHost = properties.getProperty("greenhouseCommunication.host");
            int httpPort = Integer.parseInt(properties.getProperty("greenhouseCommunication.port"));
            String mqttHost = properties.getProperty("greenhouseCommunicationMQTT.host");
            int mqttPort = Integer.parseInt(properties.getProperty("greenhouseCommunicationMQTT.port"));
            GreenhouseCommunicationHTTPModel httpModel = new GreenhouseCommunicationHTTPModel(vertx);
            GreenhouseCommunicationMQTTModel mqttModel = new GreenhouseCommunicationMQTTModel(vertx);
            vertx.deployVerticle(new GreenhouseCommunicationService(httpModel, mqttModel, mqttHost, httpHost, mqttPort, httpPort));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
