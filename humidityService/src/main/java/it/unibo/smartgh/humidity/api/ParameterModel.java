package it.unibo.smartgh.humidity.api;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.humidity.HumidityServiceLauncher;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;
import it.unibo.smartgh.plantValue.entity.Modality;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class ParameterModel implements ParameterAPI {
    private static final String PARAMETER_NAME = "humidity";
    private static final String TOPIC = "VENTILATION";
    private final PlantValueAPI plantValueModel;

    private final JsonObject thingDescription;

    public ParameterModel(PlantValueAPI plantValueModel) {
        this.plantValueModel = plantValueModel;
        this.thingDescription = new JsonObject();

        this.initializeThingDescription();
    }

    private void initializeThingDescription() {
        InputStream is = HumidityServiceLauncher.class.getResourceAsStream("/config.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String host = properties.getProperty(PARAMETER_NAME + ".host");
        int port = Integer.parseInt(properties.getProperty(PARAMETER_NAME + ".port"));

        thingDescription.put("@context", "https://webthings.io/schemas/");
        thingDescription.put("id", "http://"+ host + ":" + port +"/"+ PARAMETER_NAME);
        thingDescription.put("title", PARAMETER_NAME);
        thingDescription.put("description", "web connected system to handle the humidity of a greenhouse.");
    }

    @Override
    public Future<Void> newDataReceived(JsonObject message) {
        Promise<Void> promise = Promise.promise();
        ArrayList<Future> allFutures = new ArrayList<Future>();
        allFutures.add(this.saveData(message));
        allFutures.add(this.performOperation(message));
        CompositeFuture.all(allFutures).onComplete(res -> {
            System.out.println("Data saved.");
            promise.complete();
        });
        return promise.future();
    }

    private Future<Void> performOperation(JsonObject message) {
        Promise<Void> promise = Promise.promise();
        try {
            String id = message.getString("id");
            this.plantValueModel.getGreenhouseModality(id)
                    .onSuccess(modality -> {
                            this.plantValueModel.getParameter(id, PARAMETER_NAME)
                                    .onSuccess(parameter -> {
                                        Double min = parameter.getMin();
                                        Double max = parameter.getMax();
                                        Double value = message.getDouble("value");
                                        String status = "normal";
                                        Future<String> lastOperation = this.plantValueModel.getLastOperation(id, PARAMETER_NAME);
                                        if (value.compareTo(max) > 0){
                                            status = "alarm";
                                            if (modality.equals(Modality.AUTOMATIC)) {
                                                lastOperation.onSuccess(res -> {
                                                   if(!res.split(" ")[1].equals("on")){
                                                       this.sendOperation(id, "on");
                                                   }
                                                }).onFailure(err -> {
                                                    this.sendOperation(id,"on");
                                                });
                                            }
                                        } else {
                                            if (value.compareTo(min) < 0) status = "alarm";
                                            if (modality.equals(Modality.AUTOMATIC)) {
                                                lastOperation.onSuccess(res -> {
                                                    if(!res.split(" ")[1].equals("off")){
                                                        this.sendOperation(id, "off");
                                                    }
                                                }).onFailure(err -> {
                                                    this.sendOperation(id,"off");
                                                });
                                            }
                                        }
                                        this.plantValueModel.notifyClients(id, PARAMETER_NAME, value, status)
                                                .onSuccess(h -> promise.complete());

                                    });
                    });
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<PlantValueAPI> getPlantValueAPI() {
        Promise<PlantValueAPI> promise = Promise.promise();
        promise.complete(this.plantValueModel);
        return promise.future();
    }

    @Override
    public Future<JsonObject> getThingDescription() {
        Promise<JsonObject> promise = Promise.promise();
        promise.complete(this.thingDescription);
        return promise.future();
    }

    private void sendOperation(String id, String action) {
        this.plantValueModel.performAction(id, PARAMETER_NAME, TOPIC + " " + action, Modality.AUTOMATIC.toString());
    }

    private Future<Void> saveData(JsonObject message) {
        PlantValue value = new PlantValueImpl(message.getString("id"), new Date(), message.getDouble("value"));
        return this.plantValueModel.postValue(value);
    }
}
