package it.unibo.smartgh.greenhouse.presentation.serializer;

import com.google.gson.*;
import it.unibo.smartgh.greenhouse.entity.plant.Parameter;
import it.unibo.smartgh.greenhouse.entity.plant.ParameterImpl;

import java.lang.reflect.Type;

/**
 * Custom {@link JsonSerializer} for the {@link Parameter} class. Used to convert a {@link Parameter} object
 * into an JSON object.
 */
public class ParameterSerializer implements JsonSerializer<ParameterImpl> {
    @Override
    public JsonElement serialize(ParameterImpl src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("name", src.getName());
        json.addProperty("min", src.getMin());
        json.addProperty("max", src.getMax());
        json.addProperty("unit", src.getUnit());
        return json;
    }
}
