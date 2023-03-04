package it.unibo.smartgh.greenhouse.api;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.greenhouse.controller.GreenhouseController;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.Parameter;
import it.unibo.smartgh.greenhouse.entity.plant.ParameterType;
import it.unibo.smartgh.greenhouse.entity.plant.Plant;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Double.valueOf;
/**
 * Implementation of the Greenhouse service API.
 */
public class GreenhouseModel implements GreenhouseAPI{
    private static String CLIENT_COMMUNICATION_HOST;
    private static int CLIENT_COMMUNICATION_PORT;
    private final Vertx vertx;
    private final GreenhouseController greenhouseController;

    /**
     * Constructor of the greenhouse model.
     * @param vertx the actual vertx instance.
     * @param greenhouseController the greenhouse service controller.
     */
    public GreenhouseModel(Vertx vertx, GreenhouseController greenhouseController) {
        this.vertx = vertx;
        this.greenhouseController = greenhouseController;

        try {
            InputStream is = GreenhouseModel.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);
            CLIENT_COMMUNICATION_HOST = properties.getProperty("clientCommunication.host");
            CLIENT_COMMUNICATION_PORT = Integer.parseInt(properties.getProperty("clientCommunication.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Future<Greenhouse> getGreenhouse(String id) {
        Promise<Greenhouse> promise = Promise.promise();
        try {
            Greenhouse greenhouse = greenhouseController.getGreenhouse(id);
            promise.complete(greenhouse);
        } catch (NoSuchElementException ex) {
            promise.fail("No greenhouse");
        }
        return promise.future();
    }

    @Override
    public Future<Void> putActualModality(String id, Modality modality) {
        Promise<Void> promise = Promise.promise();
        try {
            greenhouseController.changeActualModality(id, modality);
            notifyChangeModality(id, modality);
            promise.complete();
        } catch (NoSuchElementException ex) {
            promise.fail("invalid id");
        }
        return promise.future();
    }

    @Override
    public Future<List<String>> getAllGreenhouses() {
        Promise<List<String>> promise = Promise.promise();
        promise.complete(greenhouseController.getAllGreenhousesId());
        return promise.future();
    }

    private void notifyChangeModality(String id, Modality modality){
        WebClient client = WebClient.create(vertx);
        client.post(CLIENT_COMMUNICATION_PORT, CLIENT_COMMUNICATION_HOST, "/clientCommunication/greenhouse/modality/notify")
                .sendJsonObject(
                        new JsonObject()
                                .put("greenhouseId", id)
                                .put("modality", modality)
                );
    }
}
