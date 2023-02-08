package it.unibo.smartgh.greenhouseCommunication.adapter;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.greenhouseCommunication.GreenhouseCommunicationServiceLauncher;
import it.unibo.smartgh.greenhouseCommunication.api.http.GreenhouseCommunicationHTTPAPI;
import it.unibo.smartgh.greenhouseCommunication.api.http.GreenhouseCommunicationHTTPModel;
import it.unibo.smartgh.greenhouseCommunication.api.mqtt.GreenhouseCommunicationMQTTAPI;
import it.unibo.smartgh.greenhouseCommunication.api.mqtt.GreenhouseCommunicationMQTTModel;
import it.unibo.smartgh.greenhouseCommunication.service.GreenhouseCommunicationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class GreenhouseCommunicationHTTPAdapterTest {

    private static String HTTP_HOST;
    private static int HTTP_PORT;
    private static String MQTT_HOST;
    private static int MQTT_PORT;

    @BeforeAll
    public static void start(Vertx vertx, VertxTestContext testContext){
        System.out.println("Greenhouse Communication service initializing");
        configVariable();
        GreenhouseCommunicationHTTPAPI httpModel = new GreenhouseCommunicationHTTPModel(vertx);
        GreenhouseCommunicationMQTTAPI mqttModel = new GreenhouseCommunicationMQTTModel(vertx);
        GreenhouseCommunicationService service = new GreenhouseCommunicationService(httpModel, mqttModel, MQTT_HOST, HTTP_HOST, MQTT_PORT, HTTP_PORT);
        vertx.deployVerticle(service, testContext.succeedingThenComplete());
        System.out.println("Greenhouse Communication service ready");
    }

    private static void configVariable() {
        try {
            InputStream is = GreenhouseCommunicationHTTPAdapterTest.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            HTTP_HOST = properties.getProperty("greenhouseCommunication.host");
            HTTP_PORT = Integer.parseInt(properties.getProperty("greenhouseCommunication.port"));
            MQTT_HOST = properties.getProperty("greenhouseCommunicationMQTT.host");
            MQTT_PORT = Integer.parseInt(properties.getProperty("greenhouseCommunicationMQTT.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postBrightnessOperationTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        JsonObject body = new JsonObject();
        String operationPath = "/greenhouseCommunication/brightnessOperation";
        int expectedStatusCode = 201;

        body.put("message", "LUMINOSITY 200");
        client.post(HTTP_PORT, HTTP_HOST, operationPath)
                .putHeader("content-type", "application/json")
                .sendJsonObject(body)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(response.statusCode(), expectedStatusCode);
                    testContext.completeNow();
                }));
        try {
            testContext.awaitCompletion(10, TimeUnit.SECONDS);
            System.out.println("wait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postAirHumidityOperationTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        JsonObject body = new JsonObject();
        String operationPath = "/greenhouseCommunication/humidityOperation";
        int expectedStatusCode = 201;

        body.put("message", "VENTILATION on");
        client.post(HTTP_PORT, HTTP_HOST, operationPath)
                .putHeader("content-type", "application/json")
                .sendJsonObject(body)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(response.statusCode(), expectedStatusCode);
                    testContext.completeNow();
                }));
    }

    @Test
    public void postSoilMoistureOperationTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        JsonObject body = new JsonObject();
        String operationPath = "/greenhouseCommunication/soilMoistureOperation";
        int expectedStatusCode = 201;

        body.put("message", "IRRIGATION on");
        client.post(HTTP_PORT, HTTP_HOST, operationPath)
                .putHeader("content-type", "application/json")
                .sendJsonObject(body)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(response.statusCode(), expectedStatusCode);
                    testContext.completeNow();
                }));
    }

    @Test
    public void postTemperatureOperationTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        JsonObject body = new JsonObject();
        String operationPath = "/greenhouseCommunication/temperatureOperation";
        int expectedStatusCode = 201;

        body.put("message", "TEMPERATURE decrease");
        client.post(HTTP_PORT, HTTP_HOST, operationPath)
                .putHeader("content-type", "application/json")
                .sendJsonObject(body)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(response.statusCode(), expectedStatusCode);
                    testContext.completeNow();
                }));
    }
}
