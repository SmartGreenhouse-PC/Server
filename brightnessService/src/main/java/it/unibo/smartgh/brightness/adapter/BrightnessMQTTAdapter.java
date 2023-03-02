package it.unibo.smartgh.brightness.adapter;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.messages.MqttPublishMessage;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.brightness.api.BrightnessAPI;

public class BrightnessMQTTAdapter extends AbstractAdapter<BrightnessAPI> {
    private static final String NEW_DATA_TOPIC = "Bright";
    private final String host;
    private final int port;
    private final int qos = 1;
    private MqttClient mqttClient;

    /**
     * Constructor of the brightness mqtt adapter
     * @param model of the service
     * @param host of the mqtt server
     * @param port of the mqtt server
     * @param vertx the current instance
     */
    public BrightnessMQTTAdapter(BrightnessAPI model, String host, int port, Vertx vertx){
        super(model, vertx);
        this.host = host;
        this.port = port;
    }

    @Override
    public void setupAdapter(Promise<Void> startPromise) {
        mqttClient = MqttClient.create(this.getVertx());
        mqttClient.connect(port, host, c ->{
            System.out.println("MQTT adapter connected");
            mqttClient.publishHandler(this::handleNewDataReceived)
                    .subscribe(NEW_DATA_TOPIC, qos);
        });
    }

    private void handleNewDataReceived(MqttPublishMessage s){
        System.out.println("There are new message in topic: " + s.topicName());
        JsonObject message = new JsonObject(s.payload().toString().replace('\'', '\"'));
        System.out.println(message);
        Future<Void> future = this.getModel().newDataReceived(message);
        future.onFailure(error -> {
            System.out.println(error.getMessage());
            error.printStackTrace();
        });
    }
}

