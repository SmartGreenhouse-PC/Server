package it.unibo.smartgh.operation.api;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.operation.controller.OperationController;
import it.unibo.smartgh.operation.entity.Operation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * This class provides an implementation of the {@link OperationAPI}.
 */
public class OperationModel implements OperationAPI {
    private static String COMM_SERVICE_HOST;
    private static int COMM_SERVICE_PORT;
    private static int CLIENT_SERVICE_PORT;
    private static String CLIENT_SERVICE_HOST;
    private static final String COMM_BASE_PATH = "/greenhouseCommunication";

    private static final String CLIENT_BASE_PATH = "/clientCommunication/operations/notify";
    private final OperationController operationController;
    private final Vertx vertx;

    /**
     * Creates an instance of the class with the given {@link OperationController} instance.
     * @param operationController the {@link OperationController} instance.
     * @param vertx the current instance of vertx.
     */
    public OperationModel(OperationController operationController, Vertx vertx) {
        this.operationController = operationController;
        this.vertx = vertx;
        try {
            InputStream is = OperationModel.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            CLIENT_SERVICE_HOST = properties.getProperty("clientCommunication.host");
            CLIENT_SERVICE_PORT = Integer.parseInt(properties.getProperty("clientCommunication.port"));
            COMM_SERVICE_HOST = properties.getProperty("greenhouseCommunication.host");
            COMM_SERVICE_PORT = Integer.parseInt(properties.getProperty("greenhouseCommunication.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Future<Void> postOperation(Operation operation) {
        Promise<Void> promise = Promise.promise();
        try {
            this.operationController.insertOperation(operation);
            this.notifyOperation(operation)
                    .onSuccess(promise::complete)
                    .onFailure(promise::fail);
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<List<Operation>> getOperationsInGreenhouse(String greenhouseId, int limit) {
        Promise<List<Operation>> promise = Promise.promise();
        try {
            promise.complete(this.operationController.getOperationsInGreenhouse(greenhouseId, limit));
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<List<Operation>> getParameterOperations(String greenhouseId, String parameter, int limit) {
        Promise<List<Operation>> promise = Promise.promise();
        try {
            promise.complete(this.operationController.getParameterOperations(greenhouseId, parameter, limit));
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    @Override
    public Future<List<Operation>> getOperationsInDateRange(String greenhouseId, Date from, Date to, int limit) {
        Promise<List<Operation>> promise = Promise.promise();
        try {
            promise.complete(this.operationController.getOperationsInDateRange(greenhouseId, from, to, limit));
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    private Future<Void> notifyOperation(Operation operation) {
        Promise<Void> promise = Promise.promise();
        WebClient client = WebClient.create(vertx);
        client.post(COMM_SERVICE_PORT, COMM_SERVICE_HOST, COMM_BASE_PATH + "/" + operation.getParameter() + "Operation")
                .sendJsonObject(new JsonObject().put("message", operation.getAction()))
                .onSuccess(h -> {
                    System.out.println(operation.getParameter());
                    client.post(CLIENT_SERVICE_PORT, CLIENT_SERVICE_HOST, CLIENT_BASE_PATH)
                            .sendJsonObject(new JsonObject()
                                    .put("greenhouseId", operation.getGreenhouseId())
                                    .put("parameterName", operation.getParameter())
                            )
                            .onSuccess(h2 -> promise.complete())
                            .onFailure(promise::fail);
                })
                .onFailure(promise::fail);
        return promise.future();
    }
}
