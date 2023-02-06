package it.unibo.smartgh.greenhouse.presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unibo.smartgh.greenhouse.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.greenhouse.entity.plant.ParameterImpl;
import it.unibo.smartgh.greenhouse.entity.plant.PlantImpl;
import it.unibo.smartgh.greenhouse.presentation.deserializer.GreenhouseDeserializer;
import it.unibo.smartgh.greenhouse.presentation.deserializer.ParameterDeserializer;
import it.unibo.smartgh.greenhouse.presentation.deserializer.PlantDeserializer;
import it.unibo.smartgh.greenhouse.presentation.serializer.GreenhouseSerializer;
import it.unibo.smartgh.greenhouse.presentation.serializer.ParameterSerializer;
import it.unibo.smartgh.greenhouse.presentation.serializer.PlantSerializer;


/**
 * This is a utility class to instantiate the {@link com.google.gson.JsonSerializer} and {@link com.google.gson.JsonDeserializer}
 */
public class GsonUtils {

    public static Gson createGson() {
        return new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .registerTypeAdapter(GreenhouseImpl.class, new GreenhouseSerializer())
            .registerTypeAdapter(GreenhouseImpl.class, new GreenhouseDeserializer())
            .registerTypeAdapter(PlantImpl.class, new PlantSerializer())
            .registerTypeAdapter(PlantImpl.class, new PlantDeserializer())
            .registerTypeAdapter(ParameterImpl.class, new ParameterSerializer())
            .registerTypeAdapter(ParameterImpl.class, new ParameterDeserializer())

            .create();
    }
}
