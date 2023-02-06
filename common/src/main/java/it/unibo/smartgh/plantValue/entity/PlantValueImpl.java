package it.unibo.smartgh.plantValue.entity;

import java.util.Date;
import java.util.Objects;

/**
 * Implementation of the plant value entity
 */
public class PlantValueImpl implements PlantValue {
    
    private String greenhouseId;
    private Date date;
    private Double value;

    /**
     * Empty constructor of plant value
     */
    public PlantValueImpl() {}

    /**
     * Constructor of plant value
     * @param greenhouseId id of the greenhouse
     * @param date when the value is sensed
     * @param value sensed
     */
    public PlantValueImpl(String greenhouseId, Date date, Double value) {
        this.greenhouseId = greenhouseId;
        this.date = date;
        this.value = value;
    }

    @Override
    public String getGreenhouseId() {
        return greenhouseId;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantValueImpl that = (PlantValueImpl) o;
        return Objects.equals(greenhouseId, that.greenhouseId) && Objects.equals(date, that.date) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(greenhouseId, date, value);
    }

    @Override
    public String toString() {
        return "PlantValue{" +
                "greenhouseId='" + greenhouseId + '\'' +
                ", date=" + date +
                ", value=" + value +
                '}';
    }
}
