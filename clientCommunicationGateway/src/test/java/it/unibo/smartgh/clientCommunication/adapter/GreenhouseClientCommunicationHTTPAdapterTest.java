package it.unibo.smartgh.clientCommunication.adapter;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationModel;
import it.unibo.smartgh.clientCommunication.service.ClientCommunicationService;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.api.GreenhouseModel;
import it.unibo.smartgh.greenhouse.controller.GreenhouseControllerImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.entity.plant.*;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabaseImpl;
import it.unibo.smartgh.greenhouse.presentation.GsonUtils;
import it.unibo.smartgh.greenhouse.service.GreenhouseService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests to verify the correct behaviour of the service related to the greenhouse operations.
 */
@ExtendWith(VertxExtension.class)
public class GreenhouseClientCommunicationHTTPAdapterTest {

    private static String CLIENT_COMMUNICATION_SERVICE_HOST;
    private static int CLIENT_COMMUNICATION_SERVICE_PORT;
    private static int GREENHOUSE_PORT;
    private static String GREENHOUSE_HOST;

    private static final String GREENHOUSE_ID =  "63af0ae025d55e9840cbc1fa";
    private final Map<ParameterType, Parameter> parameters = new HashMap<>(){{
        put(ParameterType.TEMPERATURE, new ParameterBuilder("temperature")
                .min(8.0)
                .max(35.0)
                .unit("\u2103")
                .build());
        put(ParameterType.BRIGHTNESS, new ParameterBuilder("brightness")
                .min(4200.0)
                .max(130000.0)
                .unit("Lux")
                .build());
        put(ParameterType.SOIL_MOISTURE, new ParameterBuilder("soilMoisture")
                .min(20.0)
                .max(65.0)
                .unit("%")
                .build());
        put(ParameterType.HUMIDITY, new ParameterBuilder("humidity")
                .min(30.0)
                .max(80.0)
                .unit("%")
                .build());
    }};

    private final Plant plant = new PlantBuilder("lemon")
            .description("is a species of small evergreen trees in the flowering plant family " +
                    "Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.")
            .image("http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg")
            .parameters(parameters)
            .build();

    private final Greenhouse greenhouse = new GreenhouseImpl(GREENHOUSE_ID, plant, Modality.MANUAL);
    private final Gson gson = GsonUtils.createGson();


    @BeforeAll
    public static void start(Vertx vertx, VertxTestContext testContext){
        System.out.println("Greenhouse service initializing");
        configVariable();
        GreenhouseAPI greenhouseModel = new GreenhouseModel(vertx, new GreenhouseControllerImpl(new GreenhouseDatabaseImpl()));
        GreenhouseService greenhouseService = new GreenhouseService(GREENHOUSE_HOST, GREENHOUSE_PORT, greenhouseModel);
        vertx.deployVerticle(greenhouseService, testContext.succeedingThenComplete());
        System.out.println("Greenhouse service ready");
        System.out.println("Client Communication service initializing");
        ClientCommunicationAPI clientCommunicationModel = new ClientCommunicationModel(vertx);
        ClientCommunicationService clientCommunicationService = new ClientCommunicationService(clientCommunicationModel,
                CLIENT_COMMUNICATION_SERVICE_HOST,
                CLIENT_COMMUNICATION_SERVICE_PORT);
        vertx.deployVerticle(clientCommunicationService, testContext.succeedingThenComplete());
        System.out.println("Client Communication service ready");
    }
    private static void configVariable() {
        File file = new File(GreenhouseClientCommunicationHTTPAdapterTest.class.getClassLoader().getResource("config.properties").getFile());
        try {
            FileInputStream fin = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fin);

            GREENHOUSE_HOST = properties.getProperty("greenhouse.host");
            GREENHOUSE_PORT = Integer.parseInt(properties.getProperty("greenhouse.port"));
            CLIENT_COMMUNICATION_SERVICE_HOST = properties.getProperty("clientCommunication.host");
            CLIENT_COMMUNICATION_SERVICE_PORT = Integer.parseInt(properties.getProperty("clientCommunication.port"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getGreenhouseInformation(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/clientCommunication/greenhouse/";
        client.get(CLIENT_COMMUNICATION_SERVICE_PORT,
                        CLIENT_COMMUNICATION_SERVICE_HOST,
                        operationPath)
                .addQueryParam("id", GREENHOUSE_ID)
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    Greenhouse resp = gson.fromJson(response.body().toString(), GreenhouseImpl.class);
                    assertEquals(greenhouse, resp);
                    testContext.completeNow();
                })));
    }

    @Test
    public void postGreenhouseModality(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/clientCommunication/greenhouse/modality";
        int expectedStatusCode = 201;
        JsonObject body = new JsonObject();
        body.put("id", GREENHOUSE_ID);
        body.put("modality", "MANUAL");
        client.post(CLIENT_COMMUNICATION_SERVICE_PORT,
                        CLIENT_COMMUNICATION_SERVICE_HOST,
                        operationPath)
                .sendJsonObject(body, testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(expectedStatusCode, response.statusCode());
                    testContext.completeNow();
                })));
    }
}

