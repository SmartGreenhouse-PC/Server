package it.unibo.smartgh.plantValue.entity;

/**
 * This interface represents a plant measured parameter
 */
public interface Parameter {
    /**
     * Get the parameter min value.
     * @return the parameter min value
     */
    Double getMin();
    /**
     * Get the parameter max value.
     * @return the parameter max value
     */
    Double getMax();
}
