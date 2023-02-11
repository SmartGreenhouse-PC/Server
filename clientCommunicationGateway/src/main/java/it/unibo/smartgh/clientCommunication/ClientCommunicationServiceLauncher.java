package it.unibo.smartgh.clientCommunication;

import io.vertx.core.Vertx;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationModel;
import it.unibo.smartgh.clientCommunication.service.ClientCommunicationService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class the represents the entry point to run the Client Communication Service.
 */
public class ClientCommunicationServiceLauncher {
    /**
     * Entry point of client communication service.
     * @param args of main
     */
    public static void  main(String[] args){
        try {
            InputStream is = ClientCommunicationServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String host = properties.getProperty("clientCommunication.host");
            int port = Integer.parseInt(properties.getProperty("clientCommunication.port"));
            Vertx vertx = Vertx.vertx();
            ClientCommunicationAPI model = new ClientCommunicationModel(vertx);
            vertx.deployVerticle(new ClientCommunicationService(model, host, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}