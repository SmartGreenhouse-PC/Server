package it.unibo.smartgh.plantValue.entity;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Implementation of the parameter entity.
 */
public class ParameterImpl implements Parameter{
    private final Double min;
    private final Double max;

    /**
     * Constructor for the parameter.
     * @param min value of the parameter
     * @param max value of the parameter
     */
    public ParameterImpl(Double min, Double max) {
        this.min = min;
        this.max = max;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterImpl parameter = (ParameterImpl) o;
        return min.equals(parameter.min) && max.equals(parameter.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }
}
