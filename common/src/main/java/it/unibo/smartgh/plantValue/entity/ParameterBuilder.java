package it.unibo.smartgh.plantValue.entity;

/**
 * Builder for the parameter entity.
 */
public class ParameterBuilder {
    private Double min;
    private Double max;

    public ParameterBuilder(){
    }

    /**
     * Parameter min value.
     * @param min of the parameter.
     * @return the builder.
     */
    public ParameterBuilder min(Double min){
        this.min = min;
        return this;
    }

    /**
     * Parameter max value.
     * @param max of the parameter.
     * @return the builder.
     */
    public ParameterBuilder max(Double max){
        this.max = max;
        return this;
    }

    /**
     * Create a new Parameter object
     * @return the {@link Parameter} created.
     */
    public Parameter build(){
        return new ParameterImpl(min, max);
    }
}
