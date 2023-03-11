package it.unibo.smartgh.greenhouseCommunication.api.mqtt;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttClient;

/**
 * This interface represents the MQTT api of the service.
 */
public interface GreenhouseCommunicationMQTTAPI {

    /**
     * Forward the new data incoming from the greenhouse microcontroller.
     *
     * @param newGreenhouseData the new data sensed.
     * @return the future representing the forward.
     */
    Future<Void> forwardNewGreenhouseData(JsonObject newGreenhouseData);

}
