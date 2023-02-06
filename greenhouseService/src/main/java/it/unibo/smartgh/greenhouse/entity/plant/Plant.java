package it.unibo.smartgh.greenhouse.entity.plant;

import java.util.Map;

/**
 * The Plant entity interface.
 */
public interface Plant {

    /**
     * The plant name.
     * @return the plant name.*/
    String getName();

    /**
     * The plant description.
     * @return the plant description.*/
    String getDescription();

    /**
     * Get the plant image link.
     * @return the plant image link.
     */
    String getImg();

    /**
     * Get the map of parameters with key the ParameterType, and value the Parameter.
     * @return the map of parameters with key the ParameterType and value the Parameter.
     */
    Map<ParameterType, Parameter> getParameters();

}
