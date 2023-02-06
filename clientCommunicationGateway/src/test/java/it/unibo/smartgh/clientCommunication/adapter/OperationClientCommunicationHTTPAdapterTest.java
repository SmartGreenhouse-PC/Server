package it.unibo.smartgh.clientCommunication.adapter;

import com.google.gson.Gson;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationModel;
import it.unibo.smartgh.clientCommunication.service.ClientCommunicationService;
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

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests to verify the correct behaviour of the service related to the greenhouse operation operations.
 */
@ExtendWith(VertxExtension.class)
public class OperationClientCommunicationHTTPAdapterTest {

    private static String CLIENT_COMMUNICATION_SERVICE_HOST;
    private static int CLIENT_COMMUNICATION_SERVICE_PORT;
    private static int OPERATION_SERVICE_PORT;
    private static String OPERATION_SERVICE_HOST;
    private static final String OPERATION_DB_NAME = "operation";
    private static final String OPERATION_COLLECTION_NAME = "operation";

    private static OperationDatabase database;
    private static int SOCKET_PORT;
    private static String SOCKET_HOST;
    private final Gson gson = GsonUtils.createGson();
    private static final String greenhouseId = "1";
    private static final String parameter = "temperature";
    private final int limit = 5;

    private static void insertOperationInDB(VertxTestContext testContext){
        final String action = "TEMPERATURE decrease";
        final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            final Date date1 = formatter.parse("01/11/2021");
            final Date date2 = formatter.parse("21/08/2022");
            final Date date3 = formatter.parse("15/02/2023");
            database.insertOperation(new OperationImpl(greenhouseId, Modality.AUTOMATIC, date1, parameter, action));
            database.insertOperation(new OperationImpl(greenhouseId, Modality.AUTOMATIC, date2, parameter, action));
            database.insertOperation(new OperationImpl(greenhouseId, Modality.MANUAL, date3, parameter, action));
        } catch (ParseException e) {
            testContext.failNow(e);
        }
    }


    @BeforeAll
    static public void start(Vertx vertx, VertxTestContext testContext) {
        System.out.println("Operations service initializing");
        configVariable();
        insertOperationInDB(testContext);
        OperationController controller = new OperationControllerImpl(database);
        OperationAPI model = new OperationModel(controller, vertx);
        vertx.deployVerticle(new OperationService(model, OPERATION_SERVICE_HOST, OPERATION_SERVICE_PORT));
        System.out.println("Operations service ready");
        System.out.println("Client Communication service initializing");
        ClientCommunicationAPI clientCommunicationModel = new ClientCommunicationModel(vertx);
        ClientCommunicationService clientCommunicationService = new ClientCommunicationService(clientCommunicationModel,
                CLIENT_COMMUNICATION_SERVICE_HOST,
                CLIENT_COMMUNICATION_SERVICE_PORT);
        vertx.deployVerticle(clientCommunicationService, testContext.succeedingThenComplete());
        System.out.println("Client Communication service ready");
    }

    private static void configVariable() {
        File file = new File(OperationClientCommunicationHTTPAdapterTest.class.getClassLoader().getResource("config.properties").getFile());
        try {
            FileInputStream fin = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fin);

            String mongodbHost = properties.getProperty("mongodb.host");
            int mongodbPort = Integer.parseInt(properties.getProperty("mongodb.port"));
            CLIENT_COMMUNICATION_SERVICE_HOST = properties.getProperty("clientCommunication.host");
            CLIENT_COMMUNICATION_SERVICE_PORT = Integer.parseInt(properties.getProperty("clientCommunication.port"));
            OPERATION_SERVICE_HOST = properties.getProperty("operation.host");
            OPERATION_SERVICE_PORT = Integer.parseInt(properties.getProperty("operation.port"));
            SOCKET_PORT = Integer.parseInt(properties.getProperty("socketOperation.port"));
            SOCKET_HOST = properties.getProperty("socket.host");

            database = new OperationDatabaseImpl(OPERATION_DB_NAME, OPERATION_COLLECTION_NAME,
                    mongodbHost, mongodbPort);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetGreenhouseOperations(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/clientCommunication/operations";
        client.get(CLIENT_COMMUNICATION_SERVICE_PORT, CLIENT_COMMUNICATION_SERVICE_HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(res -> {
                        JsonArray array = res.body().toJsonArray();
                        assertTrue(array.size() <= limit);
                        array.forEach(o -> {
                            Operation op = gson.fromJson(o.toString(), OperationImpl.class);
                            assertEquals(greenhouseId, op.getGreenhouseId());
                        });
                        testContext.completeNow();
                    });
    }

    @Test
    public void testGetParameterOperations(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        String operationPath = "/clientCommunication/operations/parameter";
        client.get(CLIENT_COMMUNICATION_SERVICE_PORT, CLIENT_COMMUNICATION_SERVICE_HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("parameterName", parameter)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
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
    public void testGetGreenhouseOperationInDateRange(Vertx vertx, VertxTestContext testContext){
        WebClient client = WebClient.create(vertx);
        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = "2022-01-01";
        String toDate = "2023-01-01";
        final Date from;
        try {
            from = formatter.parse(fromDate);
            final Date to = formatter.parse(toDate);


        String operationPath = "/clientCommunication/operations/date";
        client.get(CLIENT_COMMUNICATION_SERVICE_PORT, CLIENT_COMMUNICATION_SERVICE_HOST, operationPath)
                .addQueryParam("id", greenhouseId)
                .addQueryParam("from", fromDate)
                .addQueryParam("to", toDate)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
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

    @Test
    public void testPostNotifyNewOperation(Vertx vertx, VertxTestContext testContext){
        JsonObject expectedJson = new JsonObject()
                .put("greenhouseId", "63af0ae025d55e9840cbc1fa")
                .put("parameterName", "temperature");

        HttpClient socketClient = vertx.createHttpClient();
        socketClient.webSocket(SOCKET_PORT,
                SOCKET_HOST,
                "/",
                wsC -> {
                    WebSocket ctx = wsC.result();
                    if (ctx != null) ctx.textMessageHandler(msg -> {
                        JsonObject json = new JsonObject(msg);
                        assertEquals(expectedJson, json);
                        testContext.completeNow();
                        ctx.close();
                    });
                });


        WebClient client = WebClient.create(vertx);
        client.post(CLIENT_COMMUNICATION_SERVICE_PORT, CLIENT_COMMUNICATION_SERVICE_HOST, "/clientCommunication/operations/notify")
                .sendJsonObject(expectedJson);
    }

    @Test
    public void testPostNewOperation(Vertx vertx, VertxTestContext testContext){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        final String action = "TEMPERATURE decrease";
        JsonObject expectedJson = new JsonObject()
                .put("greenhouseId", greenhouseId)
                .put("modality", Modality.MANUAL)
                .put("date", formatter.format(new Date()))
                .put("parameter", parameter)
                .put("action", action);
        WebClient client = WebClient.create(vertx);
        client.post(CLIENT_COMMUNICATION_SERVICE_PORT, CLIENT_COMMUNICATION_SERVICE_HOST, "/clientCommunication/operations")
                .sendJsonObject(expectedJson)
                .onSuccess(r -> {
                    assertEquals(201, r.statusCode());
                    testContext.completeNow();
                });
    }
}
