package it.unibo.smartgh.greenhouseCommunication.adapter;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.messages.MqttPublishMessage;
import it.unibo.smartgh.greenhouseCommunication.api.mqtt.GreenhouseCommunicationMQTTAPI;

/**
 * Implementation of the greenhouse communication mqtt adapter.
 */
public class GreenhouseCommunicationMQTTAdapter extends AbstractAdapter<GreenhouseCommunicationMQTTAPI> {

    private static final String GREENHOUSE_NEWDATA_TOPIC = "dataSG";

    private static final String BRIGHTNESS_OPERATION_TOPIC = "LUMINOSITY";
    private static final String SOIL_MOISTURE_OPERATION_TOPIC = "IRRIGATION";
    private static final String TEMPERATURE_OPERATION_TOPIC = "TEMPERATURE";
    private static final String AIR_HUMIDITY_OPERATION_TOPIC = "VENTILATION";

    private MqttClient mqttClient;
    private final int qos = 1;
    private final String host;
    private final int port;

    /**
     * Constructor of the greenhouse communication mqtt adapter
     * @param model of the service
     * @param host of the mqtt server
     * @param port of the mqtt server
     * @param vertx the current instance
     */
    public GreenhouseCommunicationMQTTAdapter(GreenhouseCommunicationMQTTAPI model, String host, int port, Vertx vertx){
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
                    .subscribe(GREENHOUSE_NEWDATA_TOPIC, qos);
        });
        this.getVertx().eventBus().consumer(BRIGHTNESS_OPERATION_TOPIC, this::handleOperationReceived);
        this.getVertx().eventBus().consumer(SOIL_MOISTURE_OPERATION_TOPIC, this::handleOperationReceived);
        this.getVertx().eventBus().consumer(TEMPERATURE_OPERATION_TOPIC, this::handleOperationReceived);
        this.getVertx().eventBus().consumer(AIR_HUMIDITY_OPERATION_TOPIC, this::handleOperationReceived);
    }

    private void handleOperationReceived(Message<String> msg) {
        mqttClient.publish(msg.address(), Buffer.buffer(msg.body()), MqttQoS.AT_LEAST_ONCE, false, false);
    }

    private void handleNewDataReceived(MqttPublishMessage s){
        System.out.println("There are new message in topic: " + s.topicName());
        JsonObject message = new JsonObject(s.payload().toString().replace('\'', '\"'));
        Future<Void> future = this.getModel().forwardNewGreenhouseData(message);
        future.onFailure(error -> {
            System.out.println(error.getMessage());
            error.printStackTrace();
        });
    }
}
