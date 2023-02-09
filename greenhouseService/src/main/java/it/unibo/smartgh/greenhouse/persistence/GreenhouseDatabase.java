package it.unibo.smartgh.greenhouse.persistence;

import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;

import java.util.List;

/**
 * Interface for the greenhouse service database.
 */
public interface GreenhouseDatabase {
    /**
     * Update the actual modality of a greenhouse.
     * @param id of the greenhouse.
     * @param modality the new modality.
     */
    void putActualModality(String id, Modality modality);

    /**
     * Get a greenhouse from the database.
     * @param id of the greenhouse.
     * @return the greenhouse.
     */
    Greenhouse getGreenhouse(String id);

    /**
     * Gets all the greenhouses saved.
     * @return get all the greenhouse id.
     */
    List<String> getAllGreenhousesId();
}
