package it.unibo.smartgh.clientCommunication.adapter.pathManager.impl;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import it.unibo.smartgh.clientCommunication.adapter.pathManager.GreenhousePathManager;
import it.unibo.smartgh.clientCommunication.api.ClientCommunicationAPI;
import it.unibo.smartgh.clientCommunication.customException.ParameterNotFound;


/**
 * Implementation of the related interface {@link GreenhousePathManager}.
 */
public class GreenhousePathManagerImpl implements GreenhousePathManager{

    private final ClientCommunicationAPI model;

    /**
     * Public constructor for the class.
     * @param model the model of reference tha implements the API.
     */
    public GreenhousePathManagerImpl(ClientCommunicationAPI model) {
        this.model = model;
    }

    @Override
    public void handleGetGreenhouseInfo(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse response = ctx.response();
        String greenhouseID = request.getParam("id");
        if(greenhouseID != null){
            Future<JsonObject> future = this.model.getGreenhouseInformation(greenhouseID);
            future.onSuccess(greenhouseInformation -> response.end(greenhouseInformation.toBuffer()))
                    .onFailure(exception ->{
                        response.setStatusCode(500);
                        response.setStatusMessage("Internal Server error: cause " + exception.getMessage());
                        response.end();
                    });
        }else {
            response.setStatusCode(409);
            response.setStatusMessage("Bad request: some filed is missing or invalid in the provided data.");
        }
    }

    @Override
    public void handlePostGreenhouseModality(RoutingContext request) {
        HttpServerResponse response = request.response();
        Future<HttpResponse<Buffer>> future = this.model.postGreenhouseModality(request.body().asJsonObject());
        future.onSuccess(res -> {
            response.setStatusCode(res.statusCode());
            response.setStatusMessage(res.statusMessage());
            response.end();
        }).onFailure(exception -> {
            response.setStatusCode(500);
            response.setStatusMessage("Internal Server error: cause " + exception.getMessage());
            response.end();
        });
    }

    @Override
    public void handlePostNotifyGreenhouseModality(RoutingContext request) {
        HttpServerResponse response = request.response();
        response.putHeader("Content-Type", "application/json");
        Future<Void> future = this.model.postNotifyChangeModality(request.body().asJsonObject());

        future.onSuccess(result -> response.setStatusCode(201).end())
                .onFailure(exception ->{
                    response.setStatusCode(500);
                    response.setStatusMessage("Internal Server error: cause " + exception.getMessage());
                    response.end();
                });
    }
}
