package it.unibo.smartgh.greenhouse.presentation.deserializer;

import com.google.gson.*;
import it.unibo.smartgh.greenhouse.entity.plant.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom {@link JsonDeserializer} for the {@link Plant} class. Used to convert a {@link Plant} object
 * into an JSON object.
 */
public class PlantDeserializer extends GeneralDeserializer implements JsonDeserializer<Plant> {
    @Override
    public Plant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json instanceof JsonObject){
            JsonObject object = (JsonObject) json;
            String name = this.getPropertyAsString(object, "name");
            String description = this.getPropertyAsString(object, "description");
            String img = this.getPropertyAsString(object, "img");
            Map<ParameterType, Parameter> parameters = new HashMap<>();
            object.getAsJsonArray("parameters").forEach(p ->{
                Parameter param = context.deserialize(p, ParameterImpl.class);
                parameters.put(ParameterType.parameterOf(param.getName()).get(), param);
            });
            parameters.keySet().forEach(System.out::println);
            return new PlantImpl(name, description, img, parameters);
        }
        return null;
    }
}
