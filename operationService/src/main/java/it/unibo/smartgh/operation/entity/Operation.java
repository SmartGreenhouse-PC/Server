package it.unibo.smartgh.operation.entity;

import java.util.Date;

/**
 * This interface represents an operation in a greenhouse. An operation has a greenhouse id, a modality (automatic or manual),
 * a date, a parameter, an action and an optional value.
 */
public interface Operation {

    /**
     * Returns the id of the greenhouse where the operation took place.
     * @return the id of the greenhouse.
     */
    String getGreenhouseId();

    /**
     * Returns the modality of the operation (automatic or manual).
     * @return the modality of the operation.
     */
    Modality getModality();

    /**
     * Returns the date of the operation.
     * @return the date of the operation.
     */
    Date getDate();

    /**
     * Returns the parameter of the operation.
     * @return the parameter of the operation.
     */
    String getParameter();

    /**
     * Returns the action of the operation.
     * @return the action of the operation.
     */
    String getAction();

}
