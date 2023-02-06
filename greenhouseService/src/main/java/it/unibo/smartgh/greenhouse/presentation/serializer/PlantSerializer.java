package it.unibo.smartgh.greenhouse.presentation.serializer;

import com.google.gson.*;
import it.unibo.smartgh.greenhouse.entity.plant.*;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Custom {@link JsonSerializer} for the {@link Plant} class. Used to convert a {@link Plant} object
 * into an JSON object.
 */
public class PlantSerializer implements JsonSerializer<PlantImpl> {
    @Override
    public JsonElement serialize(PlantImpl src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("name", src.getName());
        json.addProperty("description", src.getDescription());
        json.addProperty("img", src.getImg());
        JsonArray jsonArray = new JsonArray();
        src.getParameters().forEach((k,v) -> {
            jsonArray.add(context.serialize(v, ParameterImpl.class));
        });
        json.add("parameters", jsonArray);
        return json;
    }
}
