package it.unibo.smartgh.brightness.api;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;

import java.util.ArrayList;
import java.util.Date;

public class BrightnessModel implements BrightnessAPI {

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
        //TODO
        return null;
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
