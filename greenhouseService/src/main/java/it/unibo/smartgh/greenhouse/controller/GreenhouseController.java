package it.unibo.smartgh.greenhouse.controller;

import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;

import java.util.List;

/**
 * Interface of the Greenhouse service controller.
 */
public interface GreenhouseController {

    /**
     * Update the actual modality of a greenhouse.
     * @param id of the greenhouse.
     * @param modality the new modality.
     */
    void changeActualModality(String id, Modality modality);

    /**
     * Get a greenhouse from the database.
     * @param id of the greenhouse.
     * @return the greenhouse.
     */
    Greenhouse getGreenhouse(String id);

    /**
     * Gets all the greenhouses id saved.
     * @return a list of the greenhouse id.
     */
    List<String> getAllGreenhousesId();
}
