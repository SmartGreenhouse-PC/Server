package it.unibo.smartgh.clientCommunication.api;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import it.unibo.smartgh.clientCommunication.api.apiOperationManager.GreenhouseAPIOperationManager;
import it.unibo.smartgh.clientCommunication.api.apiOperationManager.OperationAPIOperationManager;
import it.unibo.smartgh.clientCommunication.api.apiOperationManager.ParametersAPIOperationManager;

import java.util.Date;


/**
 * This class represnt the implementation of the interface {@link ClientCommunicationAPI}.
 */
public class ClientCommunicationModel implements ClientCommunicationAPI{

    private final GreenhouseAPIOperationManager greenhouseAPIOperationManager;
    private final ParametersAPIOperationManager parametersAPIOperationManager;
    private final OperationAPIOperationManager operationAPIOperationManager;

    /**
     * The public constructor of the class.
     * @param vertx the program's instance of Vertx.
     */
    public ClientCommunicationModel(Vertx vertx) {
        this.greenhouseAPIOperationManager = new GreenhouseAPIOperationManager(vertx);
        this.parametersAPIOperationManager = new ParametersAPIOperationManager(vertx);
        this.operationAPIOperationManager = new OperationAPIOperationManager(vertx);
    }

    @Override
    public Future<JsonObject> getGreenhouseInformation(String greenhouseID) {
        return this.greenhouseAPIOperationManager.getGreenhouseInformation(greenhouseID);
    }

    @Override
    public Future<HttpResponse<Buffer>> postGreenhouseModality(JsonObject newGreenhouseModality) {
        return this.greenhouseAPIOperationManager.postGreenhouseModality(newGreenhouseModality);
    }

    @Override
    public Future<Void> postCurrentPlantValueData(JsonObject parameterInformation) {
        return this.parametersAPIOperationManager.postCurrentPlantValueData(parameterInformation);
    }

    @Override
    public Future<JsonArray> getHistoricalData(String greenhouseID, String parameterName, int limit) {
        return this.parametersAPIOperationManager.getHistoricalData(greenhouseID, parameterName, limit);
    }

    @Override
    public Future<JsonObject> getParameterCurrentValue(String greenhouseID, String parameterName) {
        return this.parametersAPIOperationManager.getParameterCurrentValue(greenhouseID, parameterName);
    }

    @Override
    public Future<JsonArray> getGreenhouseOperations(String greenhouseID, int limit) {
        return this.operationAPIOperationManager.getGreenhouseOperations(greenhouseID, limit);
    }

    @Override
    public Future<JsonArray> getOperationsParameter(String greenhouseID, String parameterName, int limit) {
        return this.operationAPIOperationManager.getOperationsParameter(greenhouseID, parameterName, limit);
    }

    @Override
    public Future<JsonArray> getOperationsInDateRange(String greenhouseID, Date from, Date to, int limit) {
        return this.operationAPIOperationManager.getOperationsInDateRange(greenhouseID, from, to, limit);
    }

    @Override
    public Future<Void> postNotifyNewOperation(JsonObject operationInformation) {
        return this.operationAPIOperationManager.postNotifyNewOperation(operationInformation);
    }

    @Override
    public Future<Void> postNewOperation(JsonObject operation) {
        return this.operationAPIOperationManager.postNewOperation(operation);
    }

    @Override
    public Future<Void> postNotifyChangeModality(JsonObject modalityInformation) {
        return this.greenhouseAPIOperationManager.postNotifyChangeModality(modalityInformation);
    }
}
