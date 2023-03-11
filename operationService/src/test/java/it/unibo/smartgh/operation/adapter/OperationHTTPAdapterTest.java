package it.unibo.smartgh.operation.adapter;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.operation.api.OperationAPI;
import it.unibo.smartgh.operation.api.OperationModel;
import it.unibo.smartgh.operation.controller.OperationController;
import it.unibo.smartgh.operation.controller.OperationControllerImpl;
import it.unibo.smartgh.operation.entity.Modality;
import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;
import it.unibo.smartgh.operation.persistence.OperationDatabase;
import it.unibo.smartgh.operation.persistence.OperationDatabaseImpl;
import it.unibo.smartgh.operation.presentation.GsonUtils;
import it.unibo.smartgh.operation.service.OperationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class represents a test for the {@link OperationHTTPAdapter} class.
 * It checks the correct behavior of the HTTP adapter by sending requests to the corresponding uri and verifying
 * the responses.
 */
@ExtendWith(VertxExtension.class)
public class OperationHTTPAdapterTest {

    private static final String OPERATION_DB_NAME = "operation";
    private static final String OPERATION_COLLECTION_NAME = "operation";
    private static String SERVICE_HOST;
    private static int SERVICE_PORT;

    private static OperationDatabase database;
    private final Gson gson = GsonUtils.createGson();
    private static final String greenhouseId = "1";
    private static final String parameter = "temperature";
    private final int limit = 5;

    @BeforeAll
    static public void insertOperationsInDB(Vertx vertx, VertxTestContext testContext) {
        System.out.println("Operation service initializing");
        configVariables();
        final String action = "TEMPERATURE decrease";
        final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            final Date date1 = formatter.parse("01/11/2021");
            final Date date2 = formatter.parse("21/08/2022");
            final Date date3 = formatter.parse("09/01/2023");
            database.insertOperation(new OperationImpl(greenhouseId, Modality.AUTOMATIC, date1, parameter, action));
            database.insertOperation(new OperationImpl(greenhouseId, Modality.AUTOMATIC, date2, parameter, action));
            database.insertOperation(new OperationImpl(greenhouseId, Modality.MANUAL, date3, parameter, action));
        } catch (ParseException e) {
            testContext.failNow(e);
        }

        OperationController controller = new OperationControllerImpl(database);
        OperationAPI model = new OperationModel(controller, vertx);
        OperationService service = new OperationService(model, SERVICE_HOST, SERVICE_PORT);
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
            InputStream is = OperationHTTPAdapterTest.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            SERVICE_HOST = properties.getProperty("operation.host");
            SERVICE_PORT = Integer.parseInt(properties.getProperty("operation.port"));
            String dbHost = properties.getProperty("mongodb.host");
            int dbPort = Integer.parseInt(properties.getProperty("mongodb.port"));
            database = new OperationDatabaseImpl(OPERATION_DB_NAME, OPERATION_COLLECTION_NAME,
                    dbHost, dbPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetOperations(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(SERVICE_PORT, SERVICE_HOST, "/operations")
                .addQueryParam("id", greenhouseId)
                .addQueryParam("limit", String.valueOf(limit))
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    JsonArray array = res.body().toJsonArray();
                    assertTrue(array.size() <= limit);
                    array.forEach(o -> {
                        Operation op = gson.fromJson(o.toString(), OperationImpl.class);
                        assertEquals(greenhouseId, op.getGreenhouseId());
                    });
                    testContext.completeNow();
                })));
    }

    @Test
    void testGetParameterOperations(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(SERVICE_PORT, SERVICE_HOST, "/operations/parameter")
                .addQueryParam("id", greenhouseId)
                .addQueryParam("parameterName", parameter)
                .addQueryParam("limit", String.valueOf(limit))
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    JsonArray array = res.body().toJsonArray();
                    assertTrue(array.size() <= limit);
                    array.forEach(o -> {
                        Operation op = gson.fromJson(o.toString(), OperationImpl.class);
                        assertEquals(greenhouseId, op.getGreenhouseId());
                        assertEquals(parameter, op.getParameter());
                    });
                    testContext.completeNow();
                })));
    }

    @Test
    public void testGetLastParameterOperation(Vertx vertx, VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        client.get(SERVICE_PORT, SERVICE_HOST, "/operations/parameter/last")
                .addQueryParam("greenhouseId", greenhouseId)
                .addQueryParam("parameterName", parameter)
                .send(testContext.succeeding(res -> testContext.verify(() -> {
                    Operation op = gson.fromJson(res.body().toString(), OperationImpl.class);
                    assertEquals(greenhouseId, op.getGreenhouseId());
                    assertEquals(parameter, op.getParameter());
                    testContext.completeNow();
                })));
    }

    @Test
    void testGetOperationsInDateRange(Vertx vertx, VertxTestContext testContext) {
        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = "2022-01-01";
        String toDate = "2023-01-01";
        try {
            final Date from = formatter.parse(fromDate);
            final Date to = formatter.parse(toDate);

            WebClient client = WebClient.create(vertx);
            client.get(SERVICE_PORT, SERVICE_HOST, "/operations/date")
                    .addQueryParam("id", greenhouseId)
                    .addQueryParam("from", fromDate)
                    .addQueryParam("to", toDate)
                    .addQueryParam("limit", String.valueOf(limit))
                    .send(testContext.succeeding(res -> testContext.verify(() -> {
                        JsonArray array = res.body().toJsonArray();
                        assertTrue(array.size() <= limit);
                        array.forEach(o -> {
                            Operation op = gson.fromJson(o.toString(), OperationImpl.class);
                            assertEquals(greenhouseId, op.getGreenhouseId());
                            assertTrue(op.getDate().compareTo(from) > 0);
                            assertTrue(op.getDate().compareTo(to) < 0);
                        });
                        testContext.completeNow();
                    })));
        } catch (ParseException e) {
            testContext.failNow(e);
        }
    }
}