package it.unibo.smartgh.greenhouse.adapter;

import com.google.gson.Gson;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import it.unibo.smartgh.greenhouse.presentation.GsonUtils;

/**
 * This is an abstract class that defines common functionality for adapters that adapt some model API to a specific
 * protocol or interface.
 *
 * @param <API> the type of the model API.
 */
public abstract class AbstractAdapter<API>  {

	private final Vertx vertx;
	private final API model;
	/**
	 * Gson instance.
	 */
	protected final Gson gson;

	/**
	 * Creates an instance of the adapter with the given model API and Vertx instance.
	 * @param model the model API.
	 * @param vertx the Vertx instance.
	 */
	protected AbstractAdapter(API model, Vertx vertx) {
		this.model = model;
		this.vertx = vertx;
		this.gson = GsonUtils.createGson();
	}

	/**
	 * Returns the Vertx instance used by this adapter.
	 * @return the Vertx instance.
	 */
	protected Vertx getVertx() {
		return vertx;
	}

	/**
	 * Returns the model API used by this adapter.
	 * @return the model API.
	 */
	protected API getModel() {
		return model;
	}

	/**
	 * Performs any necessary setup for the adapter. This method should be implemented by subclasses.
	 * @param startPromise a promise that should be completed when the setup is done.
	 */
	abstract protected void setupAdapter(Promise<Void> startPromise);
	
}
