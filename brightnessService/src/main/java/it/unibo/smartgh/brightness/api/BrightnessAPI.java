package it.unibo.smartgh.brightness.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;

public interface BrightnessAPI {
    /**
     * Handler for the new data incoming.
     * @param message data received
     * @return the future representing the saved data.
     */
    Future<Void> newDataReceived(JsonObject message);

    /**
     * Get the plant value api
     * @return the future representing the plant value api.
     */
    Future<PlantValueAPI> getPlantValueAPI();
}
