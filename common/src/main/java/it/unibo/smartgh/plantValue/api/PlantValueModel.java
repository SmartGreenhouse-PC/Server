package it.unibo.smartgh.plantValue.api;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.entity.Modality;
import it.unibo.smartgh.plantValue.entity.Parameter;
import it.unibo.smartgh.plantValue.entity.ParameterBuilder;
import it.unibo.smartgh.plantValue.entity.PlantValue;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Implementation of the plant value model.
 */
public class PlantValueModel implements PlantValueAPI {

    private static int OPERATION_PORT;
    private static String OPERATION_HOST;

    private static String GREENHOUSE_HOST;
    private static int GREENHOUSE_PORT;

    private static String CLIENT_COMMUNICATION_HOST;
    private static int CLIENT_COMMUNICATION_PORT;
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
            GREENHOUSE_HOST = properties.getProperty("greenhouse.host");
            GREENHOUSE_PORT = Integer.parseInt(properties.getProperty("greenhouse.port"));
            CLIENT_COMMUNICATION_HOST = properties.getProperty("clientCommunication.host");
            CLIENT_COMMUNICATION_PORT = Integer.parseInt(properties.getProperty("clientCommunication.port"));
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
    public Future<Void> performAction(String ghId, String parameter, String action, String modality){
        Promise<Void> promise = Promise.promise();
        try{
            WebClient client = WebClient.create(vertx);
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            client.post(OPERATION_PORT, OPERATION_HOST, "/operations")
                    .sendJsonObject(
                            new JsonObject()
                                    .put("greenhouseId", ghId)
                                    .put("modality", modality)
                                    .put("date", formatter.format(new Date()))
                                    .put("parameter", parameter)
                                    .put("action", action)
                    ).onSuccess(res -> promise.complete());
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Parameter> getParameter(String id, String parameterName) {
        Promise<Parameter> promise = Promise.promise();
        try {
            WebClient client = WebClient.create(vertx);
            client.get(GREENHOUSE_PORT, GREENHOUSE_HOST, "/greenhouse/param")
                    .addQueryParam("id", id)
                    .addQueryParam("param", "temperature")
                    .as(BodyCodec.jsonObject())
                    .send()
                    .onSuccess(res -> {
                        promise.complete(new ParameterBuilder()
                                .min(res.body().getDouble("min"))
                                .max(res.body().getDouble("max"))
                                .build());
                    });
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Modality> getGreenhouseModality(String id) {
        Promise<Modality> promise = Promise.promise();
        try {
            WebClient client = WebClient.create(vertx);
            client.get(GREENHOUSE_PORT, GREENHOUSE_HOST, "/greenhouse/modality")
                    .addQueryParam("id", id)
                    .as(BodyCodec.jsonObject())
                    .send()
                    .onSuccess(res -> {
                        promise.complete(Modality.valueOf(res.body().getString("modality")));
                    });
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<Void> notifyClients(String id, String parameter, Double value, String status) {
        Promise<Void> promise = Promise.promise();
        WebClient client = WebClient.create(vertx);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        client.post(CLIENT_COMMUNICATION_PORT, CLIENT_COMMUNICATION_HOST, "/clientCommunication/parameter")
                .sendJsonObject(
                        new JsonObject()
                                .put("greenhouseId", id)
                                .put("parameterName", parameter)
                                .put("value", value)
                                .put("date", formatter.format(new Date()))
                                .put("status", status)
                );
        return promise.future();
    }
}
