package it.unibo.smartgh.greenhouse.entity.plant;
/**
 * Builder for the parameter entity.
 */
public class ParameterBuilder {
    private final String name;
    private Double min;
    private Double max;
    private String unit;

    /**
     * Constructor for the parameter builder.
     * @param name of the parameter
     */
    public ParameterBuilder(String name) {
        this.name = name;
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
     * Parameter unit.
     * @param unit of the parameter.
     * @return the builder.
     */
    public ParameterBuilder unit(String unit){
        this.unit = unit;
        return this;
    }

    /**
     * Create a new Parameter object
     * @return the {@link Parameter} created.
     */
    public Parameter build(){
        return new ParameterImpl(name, min, max, unit);
    }
}
