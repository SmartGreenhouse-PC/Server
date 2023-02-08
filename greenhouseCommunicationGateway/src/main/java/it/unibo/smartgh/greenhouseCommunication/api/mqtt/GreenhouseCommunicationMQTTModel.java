package it.unibo.smartgh.greenhouseCommunication.api.mqtt;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.greenhouseCommunication.GreenhouseCommunicationServiceLauncher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class represents the Model for handling the communication via HTTP.
 */
public class GreenhouseCommunicationMQTTModel implements GreenhouseCommunicationMQTTAPI{

    private static final String GREENHOUSE_PATH = "/greenhouse";
    private JsonObject thingDescription;
    private final WebClient httpClient;

    private String thingId;

    /**
     * Constructor for the greenhouse communication mqtt model.
     * @param vertx the current instance.
     */
    public GreenhouseCommunicationMQTTModel(Vertx vertx){
        this.httpClient = WebClient.create(vertx);
    }

    private void setupThingDescription(){
        thingDescription = new JsonObject();
        thingDescription.put("@context", "https://www.w3.org/2019/wot/td/v1");
        thingDescription.put("id", this.thingId);
        thingDescription.put("title", "GreenHouseAutomationSystem");

        /* security section */

        JsonArray schemas = new JsonArray();
        thingDescription.put("security", schemas );
        JsonObject noSec = new JsonObject();
        noSec.put("scheme", "nosec");
        schemas.add(noSec);

        /* affordances */

        /* properties */

        JsonObject props = new JsonObject();
        thingDescription.put("properties", props);
        JsonObject state = new JsonObject();
        props.put("state", state);
        state.put("type", "string");
        state.put("forms", new JsonArray());

        /* actions */

        JsonObject actions = new JsonObject();
        thingDescription.put("actions", actions);
        JsonObject luminosity = new JsonObject();
        actions.put("luminosity", luminosity);
        luminosity.put("forms", new JsonArray());

        JsonObject irrigation = new JsonObject();
        actions.put("irrigation", irrigation);
        irrigation.put("forms", new JsonArray());

        JsonObject temperature = new JsonObject();
        actions.put("temperature", temperature);
        temperature.put("forms", new JsonArray());

        JsonObject ventilation = new JsonObject();
        actions.put("ventilation", ventilation);
        ventilation.put("forms", new JsonArray());

        /* events */

        JsonObject events = new JsonObject();
        thingDescription.put("events", events);
        JsonObject stateChanged = new JsonObject();
        events.put("stateChanged", stateChanged);
        JsonObject data = new JsonObject();
        stateChanged.put("data", data);
        JsonObject dataType = new JsonObject();
        data.put("type", dataType);
        dataType.put("Temp", "string");
        dataType.put("Hum", "string");
        dataType.put("Bright", "string");
        dataType.put("Soil", "string");
        stateChanged.put("forms",  new JsonArray());

    }

    @Override
    public Future<JsonObject> getThingDescription() {
        Promise<JsonObject> p = Promise.promise();
        p.complete(thingDescription);
        return p.future();
    }

    @Override
    public Future<Void> setThingId(String thingId) {
        Promise<Void> p = Promise.promise();
        this.thingId = thingId;
        return p.future();
    }

    @Override
    public Future<Void> forwardNewGreenhouseData(JsonObject newGreenhouseData) {
        Promise<Void> p = Promise.promise();
        JsonObject message = new JsonObject();
        message.put("id", newGreenhouseData.remove("id"));
        message.put("parameters", newGreenhouseData);
        try {
            InputStream is = GreenhouseCommunicationServiceLauncher.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            String host = properties.getProperty("greenhouse.host");
            int port = Integer.parseInt(properties.getProperty("greenhouse.port"));
            httpClient.post(port, host, GREENHOUSE_PATH)
                    .putHeader("content-type", "application/json")
                    .sendJsonObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return p.future();
    }
}
