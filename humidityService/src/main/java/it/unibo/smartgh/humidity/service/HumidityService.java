package it.unibo.smartgh.humidity.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.adapter.AbstractAdapter;
import it.unibo.smartgh.humidity.adapter.HumidityHTTPAdapter;
import it.unibo.smartgh.humidity.api.ParameterAPI;
import it.unibo.smartgh.plantValue.api.PlantValueAPI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * Class that represent the Service, it extends the abstract class {@link AbstractVerticle} of Vertx.
 */
public class HumidityService extends AbstractVerticle {

    private List<AbstractAdapter> adapters;
    private final ParameterAPI model;
    private final String host;
    private final int port;
    /**
     * Constructor of humidity service.
     * @param model the humidity model.
     * @param host the humidity service host.
     * @param port the humidity service port.
     */
    public HumidityService(ParameterAPI model, String host, int port) {
        this.adapters = new LinkedList<>();
        this.model = model;
        this.host = host;
        this.port = port;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("HumidityService started.");
        installAdapters(startPromise);
    }
    private void installAdapters(Promise<Void> startPromise) {
        ArrayList<Future> allFutures = new ArrayList<Future>();
        allFutures.add(this.installHttpAdapter());
        CompositeFuture.all(allFutures).onComplete(res -> {
            System.out.println("Adapters installed.");
            startPromise.complete();
        });

    }

    private Future<Void> installHttpAdapter(){
        try {
            HumidityHTTPAdapter httpAdapter = new HumidityHTTPAdapter(model, this.getVertx(), host, port);
            Promise<Void> promise = Promise.promise();
            httpAdapter.setupAdapter(promise);
            Future<Void> fut = promise.future();
            fut.onSuccess(res -> {
                System.out.println("HTTP adapter installed.");
                adapters.add(httpAdapter);
            }).onFailure(f -> {
                System.out.println("HTTP adapter not installed");
            });

        }  catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("HTTP adapter installation failed.");
        }

        return Future.failedFuture("HTTP adapter not installed");
    }

}
