package it.unibo.smartgh.adapter;

import com.google.gson.Gson;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import it.unibo.smartgh.presentation.GsonUtils;

/**
 * Abstract class representing the Adapter of an API.
 * @param <API> model of the service.
 */
public abstract class AbstractAdapter<API>  {

	private final Vertx vertx;
	private final API model;
	/**
	 * Gson instance.
	 */
	protected final Gson gson;

	/**
	 * Constructor of the Abstract adapter.
	 * @param model of the service implementing the API.
	 * @param vertx the service vertx instance.
	 */
	protected AbstractAdapter(API model, Vertx vertx) {
		this.model = model;
		this.vertx = vertx;
		this.gson = GsonUtils.createGson();
	}

	/**
	 *	Get the vertx instance.
	 * @return the current vertx instance.
	 */
	protected Vertx getVertx() {
		return vertx;
	}

	/**
	 *	Get the model instance.
	 * @return service model.
	 */
	protected API getModel() {
		return model;
	}

	/**
	 * Perform the setup of the adapter.
	 * @param startPromise promise for the completion of the setup.
	 */
	abstract protected void setupAdapter(Promise<Void> startPromise);
	
}
