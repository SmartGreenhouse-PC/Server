package it.unibo.smartgh.clientCommunication.api;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;

import java.util.Date;


/**
 * This interface represents the HTTP API for the Client Communication service.
 */
public interface ClientCommunicationAPI {

    /**
     * Get the information of the greenhouse.
     * @param greenhouseID the id identifying the greenhouse.
     * @return a {@link io.vertx.core.Future} of {@link JsonObject} containing the greenhouse information required.
     */
    Future<JsonObject> getGreenhouseInformation(String greenhouseID);

    /**
     * Modify the actual greenhouse modality.
     *
     * @param newGreenhouseModality represents the data information you want to update.
     * @return a {@link io.vertx.core.Future} of {@link HttpResponse} representing the operation.
     */
    Future<HttpResponse<Buffer>> postGreenhouseModality(JsonObject newGreenhouseModality);

    /**
     * Put the current value for the plant monitored.
     *
     * @param parameterInformation represents the data of the parameter.
     * @return a {@link io.vertx.core.Future} representing the operation performed.
     */
    Future<Void> postCurrentPlantValueData(JsonObject parameterInformation);

    /**
     * Get the historical data for a specific parameter related to a certain greenhouse.
     *
     * @param greenhouseID  the id identifying the greenhouse.
     * @param parameterName the parameter of which you want to get the data.
     * @param limit       how many data you want to retrieve.
     * @return a {@link io.vertx.core.Future} of {@link JsonArray} containing the list of the data that you want to obtain.
     */
    Future<JsonArray> getHistoricalData(String greenhouseID, String parameterName, int limit);

    /**
     * Get the current value for a specific parameter related to a certain greenhouse.
     *
     * @param greenhouseID  the id identifying the greenhouse.
     * @param parameterName the parameter of which you want to get the data.
     * @return a {@link io.vertx.core.Future} of {@link JsonArray} containing the list of the data that you want to obtain.
     */
    Future<JsonObject> getParameterCurrentValue(String greenhouseID, String parameterName);

    /**
     * Get the operation performed on a specific greenhouse.
     * @param greenhouseID the id identifying the greenhouse.
     * @param limit the number of operations we want to retrieve.
     * @return a {@link JsonArray} containing the operation required
     */
    Future<JsonArray> getGreenhouseOperations(String greenhouseID, int limit);


    /**
     * Get the operation performed on a specific greenhouse for a specific parameter.
     * @param greenhouseID the id identifying the greenhouse.
     * @param parameterName the name of the parameter.
     * @param limit the number of operations we want to retrieve.
     * @return a {@link JsonArray} containing the operation required
     */
    Future<JsonArray> getOperationsParameter(String greenhouseID, String parameterName, int limit);


    /**
     * Get the operation performed on a specific greenhouse for a certain period.
     * @param greenhouseID the id identifying the greenhouse.
     * @param from from the date of start.
     * @param to the date of finish.
     * @param limit the number of operations we want to retrieve.
     * @return a {@link JsonArray} containing the operation required
     */
    Future<JsonArray> getOperationsInDateRange(String greenhouseID, Date from, Date to, int limit);

    /**
     * Notify clients of a new operation is performed.
     * @param operationInformation about the new operation.
     * @return a {@link io.vertx.core.Future} representing the operation performed.
     */
    Future<Void> postNotifyNewOperation(JsonObject operationInformation);

    /**
     * Store a new operation.
     * @param operation new operation.
     * @return a {@link io.vertx.core.Future} representing the operation performed.
     */
    Future<Void> postNewOperation(JsonObject operation);

    /**
     * Notify clients of a change of modality.
     * @param modalityInformation about the new modality.
     * @return a {@link io.vertx.core.Future} representing the operation performed.
     */
    Future<Void> postNotifyChangeModality(JsonObject modalityInformation);
}
