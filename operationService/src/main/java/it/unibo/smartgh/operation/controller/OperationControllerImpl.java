package it.unibo.smartgh.operation.controller;

import it.unibo.smartgh.operation.entity.Operation;
import it.unibo.smartgh.operation.entity.OperationImpl;
import it.unibo.smartgh.operation.persistence.OperationDatabase;

import java.util.Date;
import java.util.List;

/**
 * This class is an implementation of the {@link OperationController} interface.
 */
public class OperationControllerImpl implements OperationController {

    private final OperationDatabase operationDatabase;

    /**
     * Creates an instance of the class with the given {@link OperationDatabase} instance.
     * @param operationDatabase the {@link OperationDatabase} instance.
     */
    public OperationControllerImpl(OperationDatabase operationDatabase) {
        this.operationDatabase = operationDatabase;
    }

    @Override
    public void insertOperation(Operation operation) {
        this.operationDatabase.insertOperation(operation);
    }

    @Override
    public List<Operation> getOperationsInGreenhouse(String greenhouseId, int limit) {
        return this.operationDatabase.getOperationsInGreenhouse(greenhouseId, limit);
    }

    @Override
    public List<Operation> getParameterOperations(String greenhouseId, String parameter, int limit) {
        return this.operationDatabase.getParameterOperations(greenhouseId, parameter, limit);
    }

    @Override
    public List<Operation> getOperationsInDateRange(String greenhouseId, Date from, Date to, int limit) {
        return this.operationDatabase.getOperationsInDateRange(greenhouseId, from, to, limit);
    }

    @Override
    public Operation getLastParameterOperation(String greenhouseId, String parameterName) {
        List<Operation> operations = this.operationDatabase.getParameterOperations(greenhouseId, parameterName, 1);
        return operations.get(0);
    }
}
