package it.unibo.smartgh.temperature.adapter;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;
import it.unibo.smartgh.plantValue.api.PlantValueModel;
import it.unibo.smartgh.plantValue.controller.PlantValueController;
import it.unibo.smartgh.plantValue.controller.PlantValueControllerImpl;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabase;
import it.unibo.smartgh.plantValue.persistence.PlantValueDatabaseImpl;
import it.unibo.smartgh.presentation.GsonUtils;
import it.unibo.smartgh.temperature.api.ParameterAPI;
import it.unibo.smartgh.temperature.api.ParameterModel;
import it.unibo.smartgh.temperature.service.TemperatureService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify the correct behaviour of the HTTP adapter.
 */
@ExtendWith(VertxExtension.class)
public class TemperatureHTTPAdapterTest {
    private static final String TEMPERATURE_DB_NAME = "temperature";
    private static final String TEMPERATURE_COLLECTION_NAME = "temperature";
    private static String SERVICE_HOST;
    private static int SERVICE_PORT;

    private static PlantValueDatabase database;
    private final Gson gson = GsonUtils.createGson();
    private static final String greenhouseId = "63af0ae025d55e9840cbc1fa";
    private final int limit = 5;


    @BeforeAll
    static public void insertValues(Vertx vertx, VertxTestContext testContext){
        System.out.println("Brightness service initializing");
        configVariables();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        Double value = 20.0;
        try {
            Date date1 = formatter.parse(formatter.format(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC))));
            Date date2 = formatter.parse(formatter.format(Date.from(LocalDateTime.now().plusSeconds(10).toInstant(ZoneOffset.UTC))));
            Date date3 = formatter.parse(formatter.format(Date.from(LocalDateTime.now().plusSeconds(20).toInstant(ZoneOffset.UTC))));

            database.insertPlantValue(new PlantValueImpl(greenhouseId, date1, value));
            database.insertPlantValue(new PlantValueImpl(greenhouseId, date2, value));
            database.insertPlantValue(new PlantValueImpl(greenhouseId, date3, value));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        PlantValueController controller = new PlantValueControllerImpl(database);
        PlantValueAPI plantValueAPI = new PlantValueModel(vertx, controller);
        ParameterAPI model = new ParameterModel(plantValueAPI);
        TemperatureService service = new TemperatureService(model, SERVICE_HOST, SERVICE_PORT);
        vertx.deployVerticle(service, testContext.succeedingThenComplete());
        try {
            testContext.awaitCompletion(10, TimeUnit.SECONDS);
            System.out.println("wait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void configVariables() {
        try {
            InputStream is = TemperatureHTTPAdapterTest.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            SERVICE_HOST = properties.getProperty("temperature.host");
            SERVICE_PORT = Integer.parseInt(properties.getProperty("temperature.port"));
            String dbHost = properties.getProperty("mongodb.host");
            int dbPort = Integer.parseInt(properties.getProperty("mongodb.port"));
            database = new PlantValueDatabaseImpl(TEMPERATURE_DB_NAME, TEMPERATURE_COLLECTION_NAME,
                    dbHost, dbPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetCurrentValue(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/temperature";
        client.get(SERVICE_PORT, SERVICE_HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .putHeader("content-type", "application/json")
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    assertNotNull(res.body().toJsonObject());
                    assertEquals(greenhouseId, res.body().toJsonObject().getValue("greenhouseId"));
                    testContext.completeNow();
                })));
    }

    @Test
    public void testGetHistoryData(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/temperature/history";
        client.get(SERVICE_PORT, SERVICE_HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    assertNotNull(res.body().toJsonArray());
                    JsonArray result = res.body().toJsonArray();
                    assertTrue(result.size() <= limit);
                    result.forEach(o -> {
                        PlantValue plantValue = gson.fromJson(o.toString(), PlantValueImpl.class);
                        assertEquals(greenhouseId, plantValue.getGreenhouseId());
                    });
                    testContext.completeNow();
                })));
    }
}

