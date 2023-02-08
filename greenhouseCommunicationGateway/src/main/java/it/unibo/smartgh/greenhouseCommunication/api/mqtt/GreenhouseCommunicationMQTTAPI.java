package it.unibo.smartgh.greenhouseCommunication.api.mqtt;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * This interface represents the MQTT api of the service.
 */
public interface GreenhouseCommunicationMQTTAPI {

    /**
     * Get the thing description.
     * @param thingId the id of the device.
     * @return the json object representing the thing description.
     *
     */
    Future<JsonObject> getThingDescription(String thingId);

    /**
     * Forward the new data incoming from the greenhouse microcontroller.
     * @param newGreenhouseData the new data sensed.
     * @return the future representing the forward.
     */
    Future<Void> forwardNewGreenhouseData(JsonObject newGreenhouseData);

}
