package it.unibo.smartgh.brightness.api;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;
import it.unibo.smartgh.plantValue.entity.Modality;
import it.unibo.smartgh.plantValue.entity.Parameter;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;

import java.util.ArrayList;
import java.util.Date;

public class BrightnessModel implements BrightnessAPI {
    private static final String PARAMETER_NAME = "brightness";
    private final PlantValueAPI plantValueModel;

    public BrightnessModel(PlantValueAPI plantValueModel) {
        this.plantValueModel = plantValueModel;
    }

    @Override
    public Future<Void> newDataReceived(JsonObject message) {
        Promise<Void> promise = Promise.promise();
        ArrayList<Future> allFutures = new ArrayList<Future>();
        allFutures.add(this.saveData(message));
        allFutures.add(this.performOperation(message));
        CompositeFuture.all(allFutures).onComplete(res -> {
            System.out.println("Adapters installed.");
            promise.complete();
        });
        return promise.future();
    }

    private Future<Void> performOperation(JsonObject message) {
        Promise promise = Promise.promise();
        try {
            String id = message.getString("id");
            this.plantValueModel.getGreenhouseModality(id)
                    .onSuccess(modality -> {
                        if (modality.equals(Modality.AUTOMATIC)) {
                            this.plantValueModel.getParameter(id, PARAMETER_NAME)
                                    .onSuccess(parameter -> {
                                        Double min = parameter.getMin();
                                        Double max = parameter.getMax();
                                        Double value = message.getDouble("data");
                                        String status = "normal";
                                        if (value.compareTo(min) < 0){
                                            status = "alarm";
                                            int newBrigh = value.compareTo(255.0) >= 0 ? 255 : value.intValue() + 5;
                                            String action = "LUMINOSITY " + newBrigh;
                                            this.plantValueModel.performAction(id, PARAMETER_NAME, action, Modality.AUTOMATIC.toString());
                                        }  else if (value.compareTo(max) > 0) {
                                            status = "alarm";
                                            int newBrigh = value.compareTo(0.0) <= 0 ? 0 : value.intValue() - 5;
                                            String action = "LUMINOSITY " + newBrigh;
                                            this.plantValueModel.performAction(id, PARAMETER_NAME, action, Modality.AUTOMATIC.toString());
                                        }

                                        this.plantValueModel.notifyClients(id, PARAMETER_NAME, value, status)
                                                .onSuccess(h -> promise.complete());
                                    });
                        }
                    });
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }
    private Future<Void> saveData(JsonObject message) {
        PlantValue value = new PlantValueImpl(message.getString("id"), new Date(), message.getDouble("data"));
        return this.plantValueModel.postValue(value);
    }

    @Override
    public Future<PlantValueAPI> getPlantValueAPI() {
        Promise<PlantValueAPI> promise = Promise.promise();
        promise.complete(this.plantValueModel);
        return promise.future();
    }
}
