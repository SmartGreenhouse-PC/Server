package it.unibo.smartgh.greenhouse.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;

import java.util.List;

/**
 * Interface of the Greenhouse service API.
 */
public interface GreenhouseAPI {
    /**
     * Get greenhouse.
     * @param id greenhouse id.
     * @return the {@link io.vertx.core.Future} representing the greenhouse.
     */
    Future<Greenhouse> getGreenhouse(String id);

    /**
     * Change the greenhouse management modality.
     * @param id greenhouse id.
     * @param modality new modality.
     * @return the {@link io.vertx.core.Future} representing the operation performed.
     */
    Future<Void> putActualModality(String id, Modality modality);

    /**
     * Gets all the greenhouses id saved.
     * @return a list of the greenhouse id.
     */
    Future<List<String>> getAllGreenhouses();
}
