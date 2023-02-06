package it.unibo.smartgh.greenhouse.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import it.unibo.smartgh.greenhouse.adapter.GreenhouseHTTPAdapter;
import it.unibo.smartgh.greenhouse.api.GreenhouseAPI;

import static it.unibo.smartgh.greenhouse.Logger.log;
/**
 * Class that represent the Service, it extends the abstract class {@link AbstractVerticle} of Vertx.
 */
public class GreenhouseService extends AbstractVerticle {
    private final String host;
    private final int port;
    private final GreenhouseAPI model;

    /**
     * Constructor of humidity service.
     * @param host the host address.
     * @param port the port to which connect.
     * @param model the greenhouse model.
     */
    public GreenhouseService(String host, int port, GreenhouseAPI model) {
        this.host = host;
        this.port = port;
        this.model = model;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        installAdapters(startPromise);
        startPromise.complete();
    }

    private void installAdapters(Promise<Void> startPromise) {
        try {
            /*
             * Installing only the HTTP adapter.
             */
            GreenhouseHTTPAdapter httpAdapter = new GreenhouseHTTPAdapter(model, host, port, this.getVertx());
            Promise<Void> p = Promise.promise();
            httpAdapter.setupAdapter(p);
            Future<Void> fut = p.future();
            fut.onSuccess(res -> {
                startPromise.complete();
                log("HTTP adapter installed.");
            }).onFailure(f -> {
                startPromise.fail("HTTP adapter installation failed.");
                log("HTTP adapter not installed.");
            });
        } catch (Exception ex) {
            startPromise.fail("HTTP adapter installation failed.");
            log("HTTP adapter installation failed.");
        }
    }
}
