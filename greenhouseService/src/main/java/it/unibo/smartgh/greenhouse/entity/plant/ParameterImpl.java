package it.unibo.smartgh.greenhouse.entity.plant;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Implementation of the parameter entity.
 */
public class ParameterImpl implements Parameter{
    private final String name;
    private final Double min;
    private final Double max;
    private final String unit;

    public ParameterImpl(String name, Double min, Double max, String unit) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.unit = new String(unit.getBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Double getMin() {
        return this.min;
    }

    @Override
    public Double getMax() {
        return this.max;
    }

    @Override
    public String getUnit() {
        return this.unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterImpl parameter = (ParameterImpl) o;
        return name.equals(parameter.name) && min.equals(parameter.min) && max.equals(parameter.max) && unit.equals(parameter.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, min, max, unit);
    }
}
