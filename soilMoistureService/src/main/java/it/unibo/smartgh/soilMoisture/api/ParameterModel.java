package it.unibo.smartgh.soilMoisture.api;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;
import it.unibo.smartgh.plantValue.entity.Modality;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;

import java.util.ArrayList;
import java.util.Date;

public class ParameterModel implements ParameterAPI {
    private static final String PARAMETER_NAME = "soilMoisture";
    private static final String TOPIC = "IRRIGATION";
    private final PlantValueAPI plantValueModel;

    public ParameterModel(PlantValueAPI plantValueModel) {
        this.plantValueModel = plantValueModel;
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
                                        if (value.compareTo(min) < 0){
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
                                            if (value.compareTo(max) > 0) status = "alarm";
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

    private void sendOperation(String id, String action) {
        this.plantValueModel.performAction(id, PARAMETER_NAME, TOPIC + " " + action, Modality.AUTOMATIC.toString());
    }

    private Future<Void> saveData(JsonObject message) {
        PlantValue value = new PlantValueImpl(message.getString("id"), new Date(), message.getDouble("value"));
        return this.plantValueModel.postValue(value);
    }
}
