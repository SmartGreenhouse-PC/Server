package it.unibo.smartgh.greenhouse.entity;

/**
 * Builder for the plant entity.
 */
public final class PlantBuilder {
    private String name;
    private String description;
    private Double minTemperature;
    private Double maxTemperature;
    private Double minBrightness;
    private Double maxBrightness;
    private Double minSoilHumidity;
    private Double maxSoilHumidity;
    private Double minHumidity;
    private Double maxHumidity;

    /**
     * Constructor for the plant entity
     * @param name of the plant
     */
    public PlantBuilder(String name) {
        this.name = name;
    }

    /**
     * Add description to plant entity
     * @param description of the plant
     * @return the plant builder
     */
    public PlantBuilder description(String description) {
        this.description = description;
        return this;
    }
    /**
     * Add the minimum temperature to plant entity
     * @param val of the plant
     * @return the plant builder
     */
    public PlantBuilder minTemperature(Double val) {
        this.minTemperature = val;
        return this;
    }
    /**
     * Add the maximum temperature to plant entity
     * @param val of the plant
     * @return the plant builder
     */
    public PlantBuilder maxTemperature(Double val) {
        this.maxTemperature = val;
        return this;
    }
    /**
     * Add the minimum brightness to plant entity
     * @param val of the plant
     * @return the plant builder
     */
    public PlantBuilder minBrightness(Double val) {
        this.minBrightness = val;
        return this;
    }
    /**
     * Add the maximum brightness to plant entity
     * @param val of the plant
     * @return the plant builder
     */
    public PlantBuilder maxBrightness(Double val) {
        this.maxBrightness = val;
        return this;
    }

    /**
     * Add the minimum soil moisture to plant entity
     * @param val of the plant
     * @return the plant builder
     */
    public PlantBuilder minSoilHumidity(Double val) {
        this.minSoilHumidity = val;
        return this;
    }

    /**
     * Add the maximum soil moisture to plant entity
     * @param val of the plant
     * @return the plant builder
     */
    public PlantBuilder maxSoilHumidity(Double val) {
        this.maxSoilHumidity = val;
        return this;
    }

    /**
     * Add the minimum humidity to plant entity
     * @param val of the plant
     * @return the plant builder
     */
    public PlantBuilder minHumidity(Double val) {
        this.minHumidity = val;
        return this;
    }

    /**
     * Add the maximum humidity to plant entity
     * @param val of the plant
     * @return the plant builder
     */
    public PlantBuilder maxHumidity(Double val) {
        this.maxHumidity = val;
        return this;
    }

    /**
     * Create a new Plant entity
     * @return the new plant
     */
    public Plant build(){
        return new PlantImpl(this.name,
                this.description,
                this.minTemperature,
                this.maxTemperature,
                this.minBrightness,
                this.maxBrightness,
                this.minSoilHumidity,
                this.maxSoilHumidity,
                this.minHumidity,
                this.maxHumidity
                );
    }

}
