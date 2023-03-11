package it.unibo.smartgh.operation.api;

import io.vertx.core.Future;
import it.unibo.smartgh.operation.entity.Operation;

import java.util.Date;
import java.util.List;

/**
 * This interface defines the methods for a service that provides access to operations.
 */
public interface OperationAPI {

    /**
     * Posts the given operation to the service.
     * @param operation the operation to be posted.
     * @return a future that will contain the result of the operation.
     */
    Future<Void> postOperation(Operation operation);

    /**
     * Retrieves a list of operations in the specified greenhouse, up to the given limit.
     * @param greenhouseId the id of the greenhouse to retrieve operations for.
     * @param limit the maximum number of operations to retrieve.
     * @return a future that will contain the list of retrieved operations.
     */
    Future<List<Operation>> getOperationsInGreenhouse(String greenhouseId, int limit);

    /**
     * Retrieves a list of operations for the specified parameter in the given greenhouse, up to the given limit.
     * @param greenhouseId the id of the greenhouse to retrieve operations for.
     * @param parameter the parameter to filter the operations by.
     * @param limit the maximum number of operations to retrieve.
     * @return a future that will contain the list of retrieved operations.
     */
    Future<List<Operation>> getParameterOperations(String greenhouseId, String parameter, int limit);

    /**
     * Retrieves a list of operations in the specified greenhouse within the given date range, up to the given limit.
     * @param greenhouseId the id of the greenhouse to retrieve operations for.
     * @param from the start date of the range to filter the operations by.
     * @param to the end date of the range to filter the operations by.
     * @param limit the maximum number of operations to retrieve.
     * @return a future that will contain the list of retrieved operations.
     */
    Future<List<Operation>> getOperationsInDateRange(String greenhouseId, Date from, Date to, int limit);
    /**
     * Retrieves the last operation for the specified parameter in the given greenhouse/
     * @param greenhouseId the id of the greenhouse to retrieve operations for.
     * @param parameterName the parameter to filter the operations by.
     * @return a future that will contain the last operation.
     */
    Future<Operation> getLastParameterOperation(String greenhouseId, String parameterName);
}
