package it.unibo.smartgh.greenhouseCommunication.api.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.mqtt.MqttClient;
import it.unibo.smartgh.greenhouseCommunication.GreenhouseCommunicationServiceLauncher;
import it.unibo.smartgh.greenhouseCommunication.entity.ParameterType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class represents the Model for handling the communication via HTTP.
 */
public class GreenhouseCommunicationMQTTModel implements GreenhouseCommunicationMQTTAPI{
    private final WebClient httpClient;

    /**
     * Constructor for the greenhouse communication mqtt model.
     * @param vertx the current instance.
     */
    public GreenhouseCommunicationMQTTModel(Vertx vertx){
        this.httpClient = WebClient.create(vertx);
    }

    @Override
    public Future<Void> forwardNewGreenhouseData(JsonObject newGreenhouseData) {
        Promise<Void> p = Promise.promise();

        try {
            InputStream is = GreenhouseCommunicationServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);
            String parameterName = ParameterType
                    .parameterOfParameterTopic(newGreenhouseData.getString("param"))
                    .get().getName();

            String host = properties.getProperty(parameterName + ".host");
            int port = Integer.parseInt(properties.getProperty(parameterName + ".port"));
            httpClient.post(port, host, "/"+parameterName)
                    .putHeader("content-type", "application/json")
                    .sendJsonObject(newGreenhouseData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return p.future();
    }
}
