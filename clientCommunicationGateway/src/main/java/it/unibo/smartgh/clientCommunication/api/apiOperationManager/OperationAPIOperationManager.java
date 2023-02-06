package it.unibo.smartgh.clientCommunication.api.apiOperationManager;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Utility class to handle the request to the Operation service.
 */
public class OperationAPIOperationManager {

    private static final String OPERATION_BASE_PATH = "/operations";
    private static final String GET_OPERATION_PARAMETER = OPERATION_BASE_PATH + "/parameter";
    private static final String GET_OPERATION_IN_DATE_RANGE = OPERATION_BASE_PATH + "/date";
    private static String OPERATION_SERVICE_HOST;
    private static int OPERATION_SERVICE_PORT;
    private final WebClient httpClient;
    private final Vertx vertx;

    /**
     * Public constructor of the class.
     * @param vertx the program's instance of Vertx.
     */
    public OperationAPIOperationManager(Vertx vertx){
        this.httpClient = WebClient.create(vertx);
        this.vertx = vertx;
        try {
            InputStream is = OperationAPIOperationManager.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            OPERATION_SERVICE_HOST = properties.getProperty("operation.host");
            OPERATION_SERVICE_PORT = Integer.parseInt(properties.getProperty("operation.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send the request to the Operation service, to gets the operations performed on a specific greenhouse.
     * @param greenhouseID the greenhouse identifier.
     * @param limit the number of operations we want to retrieve.
     * @return a {@link io.vertx.core.Future}, containing the operations required.
     */
    public Future<JsonArray> getGreenhouseOperations(String greenhouseID, int limit){
        Promise<JsonArray> p = Promise.promise();
        httpClient.get(OPERATION_SERVICE_PORT, OPERATION_SERVICE_HOST, OPERATION_BASE_PATH)
                .addQueryParam("id", greenhouseID)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonArray()))
                .onFailure(p::fail);
        return p.future();
    }

    /**
     * Send the request to the Operation service, to gets the operations performed on a specific greenhouse for a specific parameter.
     * @param greenhouseID the greenhouse identifier.
     * @param parameterName the parameter name in which we are interested.
     * @param limit the number of operations we want to retrieve.
     * @return a {@link io.vertx.core.Future}, containing the operations required.
     */
    public Future<JsonArray> getOperationsParameter(String greenhouseID, String parameterName, int limit){
        Promise<JsonArray> p = Promise.promise();
        httpClient.get(OPERATION_SERVICE_PORT, OPERATION_SERVICE_HOST, GET_OPERATION_PARAMETER)
                .addQueryParam("id", greenhouseID)
                .addQueryParam("parameterName", parameterName)
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonArray()))
                .onFailure(p::fail);
        return p.future();
    }

    /**
     * Send the request to the Operation service, to gets the operations performed on a specific greenhouse in a certain period of time.
     * @param greenhouseID the greenhouse identifier.
     * @param from the date of start.
     * @param to the date of finish.
     * @param limit the number of operations we want to retrieve.
     * @return a {@link io.vertx.core.Future}, containing the operations required.
     */
    public Future<JsonArray> getOperationsInDateRange(String greenhouseID, Date from, Date to, int limit){
        Promise<JsonArray> p = Promise.promise();
        httpClient.get(OPERATION_SERVICE_PORT, OPERATION_SERVICE_HOST, GET_OPERATION_IN_DATE_RANGE)
                .addQueryParam("id", greenhouseID)
                .addQueryParam("from", from.toString())
                .addQueryParam("to", to.toString())
                .addQueryParam("limit", String.valueOf(limit))
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonArray()))
                .onFailure(p::fail);
        return p.future();
    }

    /**
     * Posts a new operation performed by the user.
     * @param operationInformation the information of the operation performed.
     * @return a {@link Future} representing the operation required.
     */
    public Future<Void> postNotifyNewOperation(JsonObject operationInformation) {
        Promise<Void> p = Promise.promise();
        vertx.eventBus().publish("operation", operationInformation.toString());
        p.complete();
        return p.future();
    }

    /**
     * Posts a new operation performed.
     * @param operation the operation executed.
     * @return a {@link Future} representing the operation required.
     */
    public Future<Void> postNewOperation(JsonObject operation) {
        Promise<Void> p = Promise.promise();
        httpClient.post(OPERATION_SERVICE_PORT, OPERATION_SERVICE_HOST, OPERATION_BASE_PATH)
                .putHeader("content-type", "application/json")
                .sendJsonObject(operation)
                .onSuccess(r -> p.complete())
                .onFailure(p::fail);
        return p.future();
    }
}
