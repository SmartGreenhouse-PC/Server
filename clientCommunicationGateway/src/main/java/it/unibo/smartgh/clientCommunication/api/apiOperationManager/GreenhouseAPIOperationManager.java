package it.unibo.smartgh.clientCommunication.api.apiOperationManager;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Utility class to handle the request to the Greenhouse service.
 */
public class GreenhouseAPIOperationManager {

    private static final String GREENHOUSE_BASE_PATH = "/greenhouse";

    private static String GREENHOUSE_SERVICE_HOST;
    private static int GREENHOUSE_SERVICE_PORT;
    private final WebClient httpClient;
    private final Vertx vertx;

    /**
     * Public constructor of the class.
     * @param vertx the program's instance of Vertx.
     */
    public GreenhouseAPIOperationManager(Vertx vertx) {
        this.vertx = vertx;
        try {
            InputStream is = GreenhouseAPIOperationManager.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(is);

            GREENHOUSE_SERVICE_HOST = properties.getProperty("greenhouse.host");
            GREENHOUSE_SERVICE_PORT = Integer.parseInt(properties.getProperty("greenhouse.port"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.httpClient = WebClient.create(vertx);
    }

    /**
     * Send the request to the Greenhouse service, to gets information abut a specific greenhouse.
     * @param greenhouseID represents the greenhouse.
     * @return the body of the response received.
     */
    public Future<JsonObject> getGreenhouseInformation(String greenhouseID){
        Promise<JsonObject> p = Promise.promise();
        httpClient.get(GREENHOUSE_SERVICE_PORT, GREENHOUSE_SERVICE_HOST, GREENHOUSE_BASE_PATH)
                .addQueryParam("id", greenhouseID)
                .putHeader("content-type", "application/json")
                .send()
                .onSuccess(response -> p.complete(response.body().toJsonObject()))
                .onFailure(p::fail);
        return p.future();
    }

    /**
     * Send the request to the Greenhouse service, to gets informatin abut a specific greenhouse.
     * @param newGreenhouseModality represents the information to send for changing the modality.
     * @return a @{link io.vertx.core.Future} representing the operation performed.
     */
    public Future<HttpResponse<Buffer>> postGreenhouseModality(JsonObject newGreenhouseModality){
        Promise<HttpResponse<Buffer>> p = Promise.promise();
        httpClient.put(GREENHOUSE_SERVICE_PORT, GREENHOUSE_SERVICE_HOST, GREENHOUSE_BASE_PATH)
                .putHeader("content-type", "application/json")
                .sendJsonObject(newGreenhouseModality)
                .onSuccess(p::complete)
                .onFailure(p::fail);
        return p.future();
    }

    /**
     * Notify a new operation performed by the user.
     * @param modalityInformation the information of the modality changed.
     * @return a {@link Future} representing the operation required.
     */
    public Future<Void> postNotifyChangeModality(JsonObject modalityInformation) {
        Promise<Void> p = Promise.promise();
        vertx.eventBus().publish("modality", modalityInformation.toString());
        p.complete();
        return p.future();
    }
}
