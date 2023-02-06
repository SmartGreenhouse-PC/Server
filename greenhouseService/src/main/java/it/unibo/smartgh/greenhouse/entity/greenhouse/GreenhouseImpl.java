package it.unibo.smartgh.greenhouse.entity.greenhouse;

import it.unibo.smartgh.greenhouse.entity.plant.Plant;

import java.util.Objects;

/**
 * Implementation of the greenhouse entity.
 */
public class GreenhouseImpl implements Greenhouse{
    private final Plant plant;
    private final String id;
    private final Modality modality;

    /**
     * Constructor for the greenhouse entity.
     * @param id of the greenhouse.
     * @param plant of the greenhouse.
     * @param modality the actual modality of management.
     */
    public GreenhouseImpl(String id, Plant plant, Modality modality) {
        this.plant = plant;
        this.modality = modality;
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Plant getPlant() {
        return this.plant;
    }

    @Override
    public Modality getActualModality() {
        return this.modality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GreenhouseImpl that = (GreenhouseImpl) o;
        return plant.equals(that.plant) && modality == that.modality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(plant, modality);
    }
}
