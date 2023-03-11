package it.unibo.smartgh.temperature.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;

public interface ParameterAPI {
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

    /**
     * Gets the thing description of the device.
     * @return the future containing the thing description.
     */
    Future<JsonObject> getThingDescription();
}
