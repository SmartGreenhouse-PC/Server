package it.unibo.smartgh.plantValue.entity;

import java.util.Date;

/**
 * Interface for the plant value entity.
 */
public interface PlantValue {

    /**
     * Get the greenhouse id.
     * @return the greenhouse id.
     */
    String getGreenhouseId();

    /**
     * Get the date when the value is sensed.
     * @return the date when the value is sensed.
     */
    Date getDate();

    /**
     * Get the plant value.
     * @return the plant value.
     */
    Double getValue();
}