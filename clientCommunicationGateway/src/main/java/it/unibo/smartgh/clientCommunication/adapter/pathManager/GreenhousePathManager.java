package it.unibo.smartgh.clientCommunication.adapter.pathManager;

import io.vertx.ext.web.RoutingContext;

/**
 * This interface represents the manager for the operation path related to the greenhouse.
 */
public interface GreenhousePathManager{

    /**
     * Handles the greenhouse information get request.
     * @param request the routing context of reference.
     */
    void handleGetGreenhouseInfo(RoutingContext request);

    /**
     * Handles the greenhouse patch modality request.
     * @param request the routing context of reference.
     */
    void handlePostGreenhouseModality(RoutingContext request);

    /**
     * Handles the greenhouse notify modality request.
     * @param request the routing context of reference.
     */
    void handlePostNotifyGreenhouseModality(RoutingContext request);
}
