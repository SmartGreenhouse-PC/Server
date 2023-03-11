package it.unibo.smartgh.plantValue.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.plantValue.entity.Modality;
import it.unibo.smartgh.plantValue.entity.Parameter;
import it.unibo.smartgh.plantValue.entity.PlantValue;

import java.util.List;

/**
 * Interface for the plant value API.
 */
public interface PlantValueAPI {

    /**
     * Get the future representing the current value.
     * @param  greenhouseId represent the id of the greenhouse.
     * @return the future representing the current value.
     */
    Future<PlantValue> getCurrentValue(String greenhouseId);

    /**
     * Insert the new plant value.
     * @param value represent the value to register.
     * @return the future representing insertion of the new value.
     */
    Future<Void> postValue(PlantValue value);

    /**
     * Get the future representing the history of plant values.
     *
     * @param greenhouseId the id of the greenhouse.
     * @param limit      entry will be selected in the history.
     * @return the future representing list of values.
     */
    Future<List<PlantValue>> getHistory(String greenhouseId, int limit);

    /**
     * Perform the corrective action
     * @param ghId greenhouse id
     * @param parameter parameter name
     * @param action to be performed
     * @param modality of greenhouse
     * @return the future representing the action performed.
     */
    Future<Void> performAction(String ghId, String parameter, String action, String modality);

    /**
     * Get the future representing a parameter
     * @param id of the greenhouse
     * @param parameterName name of the parameter
     * @return the future representing a parameter
     */
    Future<Parameter> getParameter(String id, String parameterName);

    /**
     * Get the future representing the greenhouse management modality
     * @param id of the greenhouse
     * @return the future representing the greenhouse management modality
     */
    Future<Modality> getGreenhouseModality(String id);

    /**
     * Future that represent the notification to client of a new data
     * @param id of the greenhouse
     * @param parameter name of the parameter
     * @param value actual value
     * @param status if the value is or is not in the range
     * @return the future representing the notification to client.
     */
    Future<Void> notifyClients(String id, String parameter, Double value, String status);

    Future<String> getLastOperation(String id, String parameter);
}
