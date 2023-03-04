package it.unibo.smartgh.soilMoisture;

import io.vertx.core.Vertx;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import it.unibo.smartgh.soilMoisture.service.SoilMoistureService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class the represents the entry point to run the Soil Moisture Service.
 */
public class SoilMoistureServiceLauncher {

    private static final String SOIL_MOISTURE_DB_NAME = "soilMoisture";
    private static final String SOIL_MOISTURE_COLLECTION_NAME = "soilMoistureValues";

    /**
     * Entry point for the soil moisture service.
     * @param args of main
     */
    public static void main(String[] args) {
        try {
            InputStream is = SoilMoistureServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);
            String host = properties.getProperty("soilMoisture.host");
            int port = Integer.parseInt(properties.getProperty("soilMoisture.port"));
            String mongodbHost = properties.getProperty("mongodb.host");
            int mongodbPort = Integer.parseInt(properties.getProperty("mongodb.port"));
            Vertx vertx = Vertx.vertx();
            PlantValueDatabase database = new PlantValueDatabaseImpl(SOIL_MOISTURE_DB_NAME, SOIL_MOISTURE_COLLECTION_NAME, mongodbHost, mongodbPort);
            PlantValueController controller = new PlantValueControllerImpl(database);
            PlantValueModel model = new PlantValueModel(vertx, controller);
            vertx.deployVerticle(new SoilMoistureService(model, host, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}