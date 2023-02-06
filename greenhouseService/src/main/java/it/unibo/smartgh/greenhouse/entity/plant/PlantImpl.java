package it.unibo.smartgh.greenhouse.entity.plant;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of the plant entity.
 */
public class PlantImpl implements Plant{
    private final String name;
    private final String description;
    private final String img;
    private final Map<ParameterType, Parameter> parameters;

    /**
     * Constructor of the plant entity.
     * @param name of the plant.
     * @param description of the plant.
     * @param img of the plant.
     * @param parameters of the plant.
     */
    public PlantImpl(String name,
                     String description,
                     String img,
                     Map<ParameterType, Parameter> parameters) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.parameters = parameters;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
    @Override
    public String getImg() {
        return this.img;
    }

    @Override
    public Map<ParameterType, Parameter> getParameters() {
        return Collections.unmodifiableMap(this.parameters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantImpl plant = (PlantImpl) o;
        return name.equals(plant.name) && description.equals(plant.description) && img.equals(plant.img) && parameters.equals(plant.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, img, parameters);
    }

}
