package it.unibo.smartgh.greenhouse.adapter;


import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Greenhouse;
import it.unibo.smartgh.greenhouse.entity.greenhouse.Modality;

import java.util.List;

import static it.unibo.smartgh.greenhouse.Logger.log;
import static it.unibo.smartgh.greenhouse.presentation.ToJSON.modalityToJSON;
import static it.unibo.smartgh.greenhouse.presentation.ToJSON.paramToJSON;

/**
 * Class representing the implementation of the greenhouse http adapter.
 */
public class GreenhouseHTTPAdapter extends AbstractAdapter<GreenhouseAPI>{

    private final static int STATUS_CODE_0K_NOTHING_RETURN = 201;
    private final static int STATUS_CODE_BAD_CONTENT = 400;
    private final static int STATUS_CODE_NOT_FOUND = 404;
    private static final String BASE_PATH = "/greenhouse";
    private static final String GREENHOUSE_ALL = BASE_PATH + "/all";
    private static final String PARAM_PATH = BASE_PATH + "/param";
    private static final String MODALITY_PATH =  BASE_PATH + "/modality";

    private final String host;
    private final int port;

    /**
     * Constructor of the Greenhouse http adapter.
     * @param model of the greenhouse service.
     * @param host of the greenhouse service.
     * @param port of the greenhouse service.
     * @param vertx the current vertx instance.
     */
    public GreenhouseHTTPAdapter(GreenhouseAPI model, String host, int port, Vertx vertx) {
        super(model, vertx);

        this.host = host;
        this.port = port;
    }

    @Override
    public void setupAdapter(Promise<Void> startPromise) {
        HttpServer server = this.getVertx().createHttpServer();
        Router router = Router.router(this.getVertx());

        router.route().handler(BodyHandler.create());

        try {
            router.get(BASE_PATH).handler(this::handleGetGreenhouse);
            router.put(BASE_PATH).handler(this::handlePutModality);
            router.post(BASE_PATH).handler(this::handlePostSensorData);
            router.get(GREENHOUSE_ALL).handler(this::handleGetAllGreenhouses);
            router.get(MODALITY_PATH).handler(this::handleGetModality);
            router.get(PARAM_PATH).handler(this::handleGetParamValues);

        } catch (Exception ex) {
            log("API setup failed - " + ex.toString());
            return;
        }

        server.requestHandler(router).listen(this.port, this.host);
    }

    private void handleGetAllGreenhouses(RoutingContext routingContext) {
        HttpServerResponse res = routingContext.response();
        res.putHeader("Content-Type", "application/json");
        Future<List<String>> fut = this.getModel().getAllGreenhouses();
        fut.onSuccess(gh -> res.end(gson.toJson(gh)))
                .onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());

    }

    private void handlePostSensorData(RoutingContext routingContext) {
        JsonObject body = routingContext.body().asJsonObject();
        String id = body.getString("id");
        JsonObject parameters = body.getJsonObject("parameters");
        HttpServerResponse res = routingContext.response();
        if (id == null || parameters == null) {
            res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();
        } else {
            try{
                Future<Void> fut = this.getModel().insertAndCheckParams(id, parameters);
                fut.onSuccess(gh -> {
                    res.setStatusCode(STATUS_CODE_0K_NOTHING_RETURN).end();
                });
                fut.onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());
            } catch (IllegalArgumentException ex) {
                res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();
            }

        }
    }

    private void handleGetParamValues(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String id = request.getParam("id");
        String param = request.getParam("param");
        HttpServerResponse res = routingContext.response();
        if (id == null || param == null) {
            res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();

        } else {
            res.putHeader("Content-Type", "application/json");
            Future<Greenhouse> fut = this.getModel().getGreenhouse(id);
            fut.onSuccess(gh -> {
                try {
                    res.end(paramToJSON(gh.getPlant(), param).toBuffer());
                } catch (RuntimeException ex) {
                    res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();
                }

            });
            fut.onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());
        }
    }

    private void handleGetModality(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String id = request.getParam("id");
        HttpServerResponse res = routingContext.response();
        if (id == null) {
            res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();

        } else {
            res.putHeader("Content-Type", "application/json");
            Future<Greenhouse> fut = this.getModel().getGreenhouse(id);
            fut.onSuccess(gh -> {
                res.end(modalityToJSON(gh.getActualModality()).toBuffer());
            });
            fut.onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());
        }

    }

    private void handlePutModality(RoutingContext routingContext) {
        JsonObject body = routingContext.body().asJsonObject();
        String id = body.getString("id");
        String modality = body.getString("modality");
        HttpServerResponse res = routingContext.response();
        if (id == null || modality == null) {
            res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();
        } else {
            try{
                res.putHeader("Content-Type", "application/json");
                Future<Void> fut = this.getModel().putActualModality(id, Modality.valueOf(modality.toUpperCase()));
                fut.onSuccess(gh -> {
                    res.setStatusCode(STATUS_CODE_0K_NOTHING_RETURN).end();
                });
                fut.onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());
            } catch (IllegalArgumentException ex) {
                res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();
            }

        }
    }

    private void handleGetGreenhouse(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String id = request.getParam("id");
        HttpServerResponse res = routingContext.response();
        if (id == null) {
            res.setStatusCode(STATUS_CODE_BAD_CONTENT).end();

        } else {
            res.putHeader("Content-Type", "application/json");
            Future<Greenhouse> fut = this.getModel().getGreenhouse(id);
            fut.onSuccess(gh -> {
                res.end(Buffer.buffer(gson.toJson(gh)));
            });
            fut.onFailure(err -> res.setStatusCode(STATUS_CODE_NOT_FOUND).end());
        }

    }
}
