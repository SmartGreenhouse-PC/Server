package it.unibo.smartgh.operation.entity;

import java.util.Date;
import java.util.Objects;

/**
 * This class is an implementation of the {@link Operation} interface.
 */
public class OperationImpl implements Operation {

    private String greenhouseId;
    private Modality modality;
    private Date date;
    private String parameter;
    private String action;

    /**
     * Constructs a new {@link OperationImpl} with no value associated with it.
     */
    public OperationImpl() {}

    /**
     * Constructs a new {@link OperationImpl} with the given characteristics.
     * @param greenhouseId the greenhouse id.
     * @param modality the modality of the operation (automatic or manual).
     * @param date the date of the operation.
     * @param parameter the parameter the operation refers to.
     * @param action the action represented by the operation.
     */
    public OperationImpl(String greenhouseId, Modality modality, Date date, String parameter, String action) {
        this.greenhouseId = greenhouseId;
        this.modality = modality;
        this.date = date;
        this.parameter = parameter;
        this.action = action;
    }

    @Override
    public String getGreenhouseId() {
        return greenhouseId;
    }

    @Override
    public Modality getModality() {
        return modality;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getParameter() {
        return parameter;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationImpl operation = (OperationImpl) o;
        return greenhouseId.equals(operation.greenhouseId) && modality == operation.modality && date.equals(operation.date) && parameter.equals(operation.parameter) && action.equals(operation.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(greenhouseId, modality, date, parameter, action);
    }

    @Override
    public String toString() {
        return "OperationImpl{" + "greenhouseId='" + greenhouseId + '\'' + ", modality=" + modality + ", date=" + date + ", parameter='" + parameter + '\'' + ", action='" + action + '}';
    }
}
