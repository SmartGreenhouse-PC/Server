package it.unibo.smartgh.brightness;

import io.vertx.core.Vertx;
import it.unibo.smartgh.brightness.api.BrightnessAPI;
import it.unibo.smartgh.brightness.api.BrightnessModel;
import it.unibo.smartgh.brightness.service.BrightnessService;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class the represents the entry point to run the Brightness Service.
 */
public class BrightnessServiceLauncher {

    private static final String BRIGHTNESS_DB_NAME = "brightness";
    private static final String BRIGHTNESS_COLLECTION_NAME = "brightnessValues";

    /**
     * Entry point.
     * @param args of main.
     */
    public static void main(String[] args) {
        try {
            InputStream is = BrightnessServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String host = properties.getProperty("brightness.host");
            int port = Integer.parseInt(properties.getProperty("brightness.port"));
            String mongodbHost = properties.getProperty("mongodb.host");
            int mongodbPort = Integer.parseInt(properties.getProperty("mongodb.port"));
            Vertx vertx = Vertx.vertx();
            PlantValueDatabase database = new PlantValueDatabaseImpl(BRIGHTNESS_DB_NAME, BRIGHTNESS_COLLECTION_NAME, mongodbHost, mongodbPort);
            PlantValueController controller = new PlantValueControllerImpl(database);
            PlantValueModel plantValueModel = new PlantValueModel(vertx, controller);
            BrightnessAPI model = new BrightnessModel(plantValueModel);
            vertx.deployVerticle(new BrightnessService(model, host, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
