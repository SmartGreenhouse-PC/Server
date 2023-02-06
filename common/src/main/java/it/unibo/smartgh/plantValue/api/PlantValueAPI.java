package it.unibo.smartgh.plantValue.api;

import io.vertx.core.Future;
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
}
