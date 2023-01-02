package it.unibo.smartgh.greenhouse.entity;

public interface Greenhouse {
    /**
     * Get the plant grown in the greenhouse
     * @return the plant grown in the greenhouse
     */
    Plant getPlant();
    /**
     * Get the actual greenhouse modality of management.
     * @return the actual greenhouse modality of management.
     */
    Modality getActualModality();
}