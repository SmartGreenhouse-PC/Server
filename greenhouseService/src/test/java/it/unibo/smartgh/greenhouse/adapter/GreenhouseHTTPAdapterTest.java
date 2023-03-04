package it.unibo.smartgh.greenhouse.adapter;

import com.google.gson.Gson;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.brightness.service.BrightnessService;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationModel;
import it.unibo.smartgh.clientCommunication.service.ClientCommunicationService;
import it.unibo.smartgh.greenhouse.entity.plant.*;
import it.unibo.smartgh.greenhouse.service.GreenhouseService;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.api.GreenhouseModel;
import it.unibo.smartgh.greenhouse.controller.GreenhouseControllerImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;
import it.unibo.smartgh.greenhouse.persistence.GreenhouseDatabaseImpl;
import it.unibo.smartgh.greenhouse.presentation.GsonUtils;
import it.unibo.smartgh.humidity.service.HumidityService;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import it.unibo.smartgh.soilMoisture.service.SoilMoistureService;
import it.unibo.smartgh.temperature.service.TemperatureService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static it.unibo.smartgh.greenhouse.presentation.ToJSON.modalityToJSON;
import static it.unibo.smartgh.greenhouse.presentation.ToJSON.paramToJSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to verify the correct behaviour of the {@link GreenhouseHTTPAdapter}.
 */
@ExtendWith(VertxExtension.class)
public class GreenhouseHTTPAdapterTest {
    private static final String ID = "greenhouse2";
    private static final String ID_AUTOMATIC =  "greenhouse3";
    private static String HOST;
    private static int PORT;
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
    private final Plant plant = new PlantBuilder("lemon AUTOMATIC")
                .description("is a species of small evergreen trees in the flowering plant family " +
                                     "Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.")
                .image("http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg")
                .parameters(parameters)
                .build();

    private final Greenhouse greenhouse = new GreenhouseImpl(ID_AUTOMATIC, plant, Modality.AUTOMATIC);

    private final Gson gson = GsonUtils.createGson();
    @BeforeAll
    public static void start(Vertx vertx, VertxTestContext testContext){
        configVariable();
        System.out.println("Greenhouse service initializing");
        GreenhouseAPI model = new GreenhouseModel(vertx, new GreenhouseControllerImpl(new GreenhouseDatabaseImpl()));
        GreenhouseService service = new GreenhouseService(HOST, PORT, model);
        vertx.deployVerticle(service, testContext.succeedingThenComplete());
        System.out.println("Greenhouse service ready");
    }

    private static void configVariable() {
        File file = new File(GreenhouseService.class.getClassLoader().getResource("config.properties").getFile());
        try {
            FileInputStream fin = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fin);

            HOST = properties.getProperty("greenhouse.host");
            PORT = Integer.parseInt(properties.getProperty("greenhouse.port"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getGreenhouseTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, "/greenhouse")
                .addQueryParam("id", ID_AUTOMATIC)
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(gson.toJson(greenhouse), response.body());
                    testContext.completeNow();
                })));
    }

    @RepeatedTest(2)
    public void putActualModalityTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.put(PORT, HOST, "/greenhouse")
                .sendJsonObject(
                new JsonObject()
                        .put("id", ID)
                        .put("modality", "MANUAL"))
                .onSuccess(response -> {
                    assertEquals(201, response.statusCode());
                    testContext.completeNow();
                });
    }
    @Test
    public void getModalityTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, "/greenhouse/modality")
                .addQueryParam("id", ID_AUTOMATIC)
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(modalityToJSON(Modality.AUTOMATIC).toString(), response.body());
                    testContext.completeNow();
                })));
    }

    @Test
    public void getParamTest(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, "/greenhouse/param")
                .addQueryParam("id", ID_AUTOMATIC)
                .addQueryParam("param", "temperature")
                .as(BodyCodec.string())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(paramToJSON(greenhouse.getPlant(), "temperature").toString(), response.body());
                    testContext.completeNow();
                })));
    }

    @Test
    public void getAllGreenhouseTest(Vertx vertx, VertxTestContext testContext) {
        Set<String> expected = new HashSet<>(){{
            add("greenhouse1");
            add("greenhouse2");
            add("greenhouse3");
        }};
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, "/greenhouse/all")
                .as(BodyCodec.jsonArray())
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(expected, response.body().stream().collect(Collectors.toSet()));
                    testContext.completeNow();
                })));
    }
}
