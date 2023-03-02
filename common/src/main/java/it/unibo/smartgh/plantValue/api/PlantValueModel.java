package it.unibo.smartgh.plantValue.api;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.entity.PlantValue;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implementation of the plant value model.
 */
public class PlantValueModel implements PlantValueAPI {

    private static int OPERATION_PORT;
    private static String OPERATION_HOST;
    private final PlantValueController plantValueController;
    private final Vertx vertx;

    /**
     * Constructor of the plant value model.
     * @param plantValueController  the plant value controller.
     */
    public PlantValueModel(Vertx vertx, PlantValueController plantValueController) {
        this.plantValueController = plantValueController;
        this.vertx = vertx;

        try {
            InputStream is = PlantValueModel.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);
            OPERATION_HOST = properties.getProperty("operation.host");
            OPERATION_PORT = Integer.parseInt(properties.getProperty("operation.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Future<PlantValue> getCurrentValue(String greenhouseId) {
        Promise<PlantValue> promise = Promise.promise();
        try {
            PlantValue brightnessValue = plantValueController.getCurrentValue(greenhouseId);
            promise.complete(brightnessValue);
        } catch (EmptyDatabaseException e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Void> postValue(PlantValue brightnessValue) {
        Promise<Void> promise = Promise.promise();
        try {
            plantValueController.insertPlantValue(brightnessValue);
        } catch (Exception e) {
            promise.fail(e);
        }
        promise.complete();
        return promise.future();
    }

    @Override
    public Future<List<PlantValue>> getHistory(String greenhouseId, int limit) {
        Promise<List<PlantValue>> promise = Promise.promise();
        try {
            promise.complete(plantValueController.getHistoryData(greenhouseId, limit));
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Void> performAction(JsonObject action) {
        Promise<Void> promise = Promise.promise();
        try {
            WebClient client = WebClient.create(vertx);
            client.post(OPERATION_PORT, OPERATION_HOST, "/operations")
                    .sendJsonObject(action);
            promise.complete();
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }


}
