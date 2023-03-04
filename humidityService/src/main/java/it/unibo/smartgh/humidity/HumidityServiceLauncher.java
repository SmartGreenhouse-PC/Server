package it.unibo.smartgh.humidity;

import io.vertx.core.Vertx;
import it.unibo.smartgh.humidity.service.HumidityService;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class the represents the entry point to run the Humidity Service.
 */
public class HumidityServiceLauncher {

    private static final String HUMIDITY_DB_NAME = "humidity";
    private static final String HUMIDITY_COLLECTION_NAME = "humidityValues";

    /**
     * Entry point for the humidity service
     * @param args of main
     */
    public static void main(String[] args) {
        try {
            InputStream is = HumidityServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String host = properties.getProperty("humidity.host");
            int port = Integer.parseInt(properties.getProperty("humidity.port"));
            String mongodbHost = properties.getProperty("mongodb.host");
            int mongodbPort = Integer.parseInt(properties.getProperty("mongodb.port"));
            Vertx vertx = Vertx.vertx();
            PlantValueDatabase database = new PlantValueDatabaseImpl(HUMIDITY_DB_NAME, HUMIDITY_COLLECTION_NAME, mongodbHost, mongodbPort);
            PlantValueController controller = new PlantValueControllerImpl(database);
            PlantValueModel model = new PlantValueModel(vertx, controller);
            vertx.deployVerticle(new HumidityService(model, host, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
