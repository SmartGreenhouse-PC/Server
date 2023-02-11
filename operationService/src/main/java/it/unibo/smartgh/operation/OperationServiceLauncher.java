package it.unibo.smartgh.operation;

import io.vertx.core.Vertx;
import it.unibo.smartgh.operation.api.OperationAPI;
import it.unibo.smartgh.operation.api.OperationModel;
import it.unibo.smartgh.operation.controller.OperationController;
import it.unibo.smartgh.operation.controller.OperationControllerImpl;
import it.unibo.smartgh.operation.persistence.OperationDatabase;
import it.unibo.smartgh.operation.persistence.OperationDatabaseImpl;
import it.unibo.smartgh.operation.service.OperationService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Launcher class for an {@link OperationService} in a Vert.x application.
 */
public class OperationServiceLauncher {

    private static final String OPERATION_DB_NAME = "operation";
    private static final String OPERATION_COLLECTION_NAME = "operation";

    /**
     * Operation service entry point.
     * @param args of main.
     */
    public static void main(String[] args) {
        try {
            InputStream is = OperationServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String host = properties.getProperty("operation.host");
            int port = Integer.parseInt(properties.getProperty("operation.port"));
            String mongodbHost = properties.getProperty("mongodb.host");
            int mongodbPort = Integer.parseInt(properties.getProperty("mongodb.port"));
            Vertx vertx = Vertx.vertx();
            OperationDatabase database = new OperationDatabaseImpl(OPERATION_DB_NAME, OPERATION_COLLECTION_NAME, mongodbHost, mongodbPort);
            OperationController controller = new OperationControllerImpl(database);
            OperationAPI model = new OperationModel(controller, vertx);
            vertx.deployVerticle(new OperationService(model, host, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
