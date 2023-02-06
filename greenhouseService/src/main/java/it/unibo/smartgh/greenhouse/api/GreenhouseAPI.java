package it.unibo.smartgh.greenhouse.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;

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
     * Store the new sensed values, check alarm situation and perform corrective actions.
     * @param id greenhouse id.
     * @param parameters the sensed values.
     * @return the {@link io.vertx.core.Future} representing the operation performed.
     */
    Future<Void> insertAndCheckParams(String id, JsonObject parameters);
}
