package it.unibo.smartgh.operation.controller;

import it.unibo.smartgh.operation.entity.Operation;

import java.util.Date;
import java.util.List;

/**
 * This interface represents a controller class for the operation service.
 */
public interface OperationController {

    /**
     * Inserts the given operation.
     * @param operation the operation to insert.
     */
    void insertOperation(Operation operation);

    /**
     * Returns a list of operations in the given greenhouse, limited to the given number of operations.
     * @param greenhouseId the id of the greenhouse.
     * @param limit the maximum number of operations to return.
     * @return a list of operations.
     */
    List<Operation> getOperationsInGreenhouse(String greenhouseId, int limit);

    /**
     * Returns a list of operations in the given greenhouse for the given parameter, limited to the given number of operations.
     * @param greenhouseId the id of the greenhouse.
     * @param parameter the parameter for which to return operations.
     * @param limit the maximum number of operations to return.
     * @return a list of operations.
     */
    List<Operation> getParameterOperations(String greenhouseId, String parameter, int limit);

    /**
     * Returns a list of operations in the given greenhouse within the given date range, limited to the given number of operations.
     * @param greenhouseId the id of the greenhouse.
     * @param from the start date of the range.
     * @param to the end date of the range.
     * @param limit the maximum number of operations to return.
     * @return a list of operations.
     */
    List<Operation> getOperationsInDateRange(String greenhouseId, Date from, Date to, int limit);
   /**
     * Retrieves the last operation for the specified parameter in the given greenhouse/
     * @param greenhouseId the id of the greenhouse to retrieve operations for.
     * @param parameterName the parameter to filter the operations by.
     * @return the last operation.
     */
    Operation getLastParameterOperation(String greenhouseId, String parameterName);
}
