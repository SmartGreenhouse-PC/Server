package it.unibo.smartgh.operation.presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unibo.smartgh.operation.entity.OperationImpl;
import it.unibo.smartgh.operation.presentation.deserializer.OperationDeserializer;
import it.unibo.smartgh.operation.presentation.serializer.OperationSerializer;


/**
 * This is a utility class to instantiate the {@link com.google.gson.JsonSerializer} and {@link com.google.gson.JsonDeserializer}
 */
public class GsonUtils {
    /**
     * Create a Gson with custom serializer and deserializer.
     * @return a gson instance
     */
    public static Gson createGson() {
        return new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .registerTypeAdapter(OperationImpl.class, new OperationSerializer())
            .registerTypeAdapter(OperationImpl.class, new OperationDeserializer())
            .create();
    }
}
