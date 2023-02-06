package it.unibo.smartgh.greenhouse.presentation.deserializer;

import com.google.gson.*;
import it.unibo.smartgh.greenhouse.entity.plant.Parameter;
import it.unibo.smartgh.greenhouse.entity.plant.ParameterBuilder;

import java.lang.reflect.Type;
/**
 * Custom {@link JsonDeserializer} for the {@link Parameter} class. Used to convert a {@link Parameter} object
 * into an JSON object.
 */
public class ParameterDeserializer extends GeneralDeserializer implements JsonDeserializer<Parameter> {
    @Override
    public Parameter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json instanceof JsonObject){
            JsonObject object = (JsonObject) json;
            String name = this.getPropertyAsString(object, "name");
            Double min = this.getPropertyAs(object, "min", Double.class, context);
            Double max = this.getPropertyAs(object, "max", Double.class, context);
            String unit = this.getPropertyAsString(object, "unit");

            return new ParameterBuilder(name).min(min).max(max).unit(unit).build();
        }else {
            throw new JsonParseException("Not a valid parameter: ");
        }
    }
}
