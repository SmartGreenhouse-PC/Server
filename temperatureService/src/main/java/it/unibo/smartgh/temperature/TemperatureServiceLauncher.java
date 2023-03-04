package it.unibo.smartgh.temperature;

import io.vertx.core.Vertx;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import it.unibo.smartgh.temperature.api.ParameterAPI;
import it.unibo.smartgh.temperature.api.ParameterModel;
import it.unibo.smartgh.temperature.service.TemperatureService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class the represents the entry point to run the Temperature Service.
 */
public class TemperatureServiceLauncher {
    private static final String TEMPERATURE_DB_NAME = "temperature";
    private static final String TEMPERATURE_COLLECTION_NAME = "temperatureValues";

    /**
     * Entry point for the temperature service.
     * @param args of main
     */
    public static void main(String[] args) {
        try {
            InputStream is = TemperatureServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String host = properties.getProperty("temperature.host");
            int port = Integer.parseInt(properties.getProperty("temperature.port"));
            String mongodbHost = properties.getProperty("mongodb.host");
            int mongodbPort = Integer.parseInt(properties.getProperty("mongodb.port"));
            Vertx vertx = Vertx.vertx();
            PlantValueDatabase database = new PlantValueDatabaseImpl(TEMPERATURE_DB_NAME, TEMPERATURE_COLLECTION_NAME, mongodbHost, mongodbPort);
            PlantValueController controller = new PlantValueControllerImpl(database);
            PlantValueModel plantValueAPI = new PlantValueModel(vertx, controller);
            ParameterAPI model = new ParameterModel(plantValueAPI);
            vertx.deployVerticle(new TemperatureService(model, host, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
