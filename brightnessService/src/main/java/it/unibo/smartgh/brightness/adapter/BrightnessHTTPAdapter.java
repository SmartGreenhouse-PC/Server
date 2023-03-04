package it.unibo.smartgh.brightness.adapter;

import com.google.gson.JsonParseException;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.brightness.api.ParameterAPI;
import it.unibo.smartgh.customException.EmptyDatabaseException;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;
import it.unibo.smartgh.plantValue.entity.PlantValue;
import it.unibo.smartgh.plantValue.entity.PlantValueImpl;

import java.util.List;
/**
 * Class that represents the HTTP adapter of the Brightness.
 */
public class BrightnessHTTPAdapter extends AbstractAdapter<ParameterAPI> {

    private static final String BASE_PATH = "/brightness";
    private static final String HISTORY_PATH = BASE_PATH + "/history";
    private static final String BAD_REQUEST_MESSAGE = "Bad request: some field is missing or invalid in the provided data.";

    private final String host;
    private final int port;
    private PlantValueAPI plantValueAPI;
    /**
     * Constructor of {@link BrightnessHTTPAdapter}.
     * @param model the brightness API model.
     * @param vertx the current instance of vertx.
     * @param host the brightness service host
     * @param port the brightness service port
     */
    public BrightnessHTTPAdapter(ParameterAPI model, Vertx vertx, String host, int port) {
        super(model, vertx);
        this.host = host;
        this.port = port;
        this.getModel().getPlantValueAPI().onSuccess(api -> {
            this.plantValueAPI = api;
        });
    }

    @Override
    public void setupAdapter(Promise<Void> startPromise) {
        HttpServer server = getVertx().createHttpServer();
        Router router = Router.router(this.getVertx());
        try{
            router.route().handler(BodyHandler.create());

            router.get(BASE_PATH).handler(this::handleGetBrightnessCurrentValue);
            router.get(HISTORY_PATH).handler(this::handleGetBrightnessHistoryData);

            router.post(BASE_PATH).handler(this::handlePostBrightnessValue);

            server.requestHandler(router).listen(port, host, http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP Thing Adapter started on port " + port);
                } else {
                    System.out.println("HTTP Thing Adapter failure " + http.cause());
                    startPromise.fail(http.cause());
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("API setup failed - " + ex);
            startPromise.fail("API setup failed - " + ex);
        }
    }

    private void handleGetBrightnessCurrentValue(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        HttpServerResponse res = ctx.response();
        res.putHeader("Content-Type", "application/json");
        String greenhouseId = request.getParam("id");
        if(greenhouseId == null){
            res.setStatusCode(409);
            res.setStatusMessage(BAD_REQUEST_MESSAGE);
            res.end();
        }else {
            Future<PlantValue> fut = this.plantValueAPI.getCurrentValue(greenhouseId);
            fut.onSuccess(brightnessValue -> res.end(gson.toJson(brightnessValue, PlantValueImpl.class)))
                    .onFailure(exception -> handleFailureInGetMethod(res, exception));
        }
    }

    private void handleGetBrightnessHistoryData(RoutingContext ctx){
        HttpServerRequest request = ctx.request();
        HttpServerResponse res = ctx.response();
        String greenhouseId = request.getParam("id");
        String limit = request.getParam("limit");
        if(greenhouseId == null || limit == null) {
            res.setStatusCode(409);
            res.setStatusMessage(BAD_REQUEST_MESSAGE);
            res.end();
        } else {
            res.putHeader("Content-Type", "application/json");
            Future<List<PlantValue>> fut = this.plantValueAPI.getHistory(greenhouseId, Integer.parseInt(limit));
            fut.onSuccess(list -> res.end(gson.toJson(list)))
                    .onFailure(exception -> handleFailureInGetMethod(res, exception));
        }
    }

    private void handlePostBrightnessValue(RoutingContext request) {
        HttpServerResponse response = request.response();
        response.putHeader("content-type", "application/json");
        try {
            Future<Void> fut = this.getModel().newDataReceived(request.body().asJsonObject());

            fut.onSuccess(res -> {
                response.setStatusCode(201);
                response.end();
            }).onFailure(exception -> {
                response.setStatusCode(500);
                response.setStatusMessage("Internal Server error: cause" + exception.getMessage());
                response.end();
            });
        } catch (JsonParseException e) {
             response.setStatusCode(400);
             response.setStatusMessage(BAD_REQUEST_MESSAGE);
             response.end();
        }
    }

    private void handleFailureInGetMethod(HttpServerResponse res, Throwable exception) {
        if (exception instanceof EmptyDatabaseException) {
            res.setStatusCode(200);
            res.end();
        } else {
            res.setStatusCode(500);
            res.setStatusMessage("Internal Server error: cause" + exception.getMessage());
            res.end();
        }
    }
}
